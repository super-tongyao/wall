package cn.ityao.wall.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.ityao.wall.entity.TResource;
import cn.ityao.wall.mapper.TResourceMapper;
import cn.ityao.wall.service.ITOptionService;
import cn.ityao.wall.service.ITResourceService;
import cn.ityao.wall.util.FileUtils;
import cn.ityao.wall.util.StringUtils;
import cn.ityao.wall.util.picture.CompressUtils;
import cn.ityao.wall.util.video.VideoUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Service
public class TResourceServiceImpl extends ServiceImpl<TResourceMapper, TResource> implements ITResourceService {

    @Autowired
    private ITOptionService itOptionService;

    @Autowired TResourceMapper tResourceMapper;

    @Override
    public List<TResource> selectAll(String tagId) {
        return tResourceMapper.selectAll(tagId);
    }

    @Override
    public void uploadFileAndSave(TResource tResource, MultipartFile cover, MultipartFile resource, HttpServletRequest request) {
        FileUtils fileUtils = new FileUtils();
        String saveFilePath = itOptionService.getOption("saveFilePath");

        // 资源后缀
        String resourceSuffix = fileUtils.getFileSuffix(resource).toLowerCase();
        if (resourceSuffix.equals("jpeg")){
            resourceSuffix = "jpg";
        }
        if (!resourceSuffix.equals("jpg") && !resourceSuffix.equals("png") &&
            !resourceSuffix.equals("gif") &&
            !resourceSuffix.equals("mp4") && !resourceSuffix.equals("mov")){
            throw new RuntimeException("图片或视频只支持jpg、png、gif、mp4、mov格式！");
        }

        // 统一声明好封面和资源的文件名
        String uuid = StringUtils.getUUID();

        // 封面文件名
        String coverFileName = uuid +"_cover.jpg";
        // 资源文件名
        String resourceFileName = uuid + "." + resourceSuffix;

        // 封面完整的存储路径
        String coverPath = saveFilePath+coverFileName;
        // 资源完整的存储路径
        String resourcePath = saveFilePath+resourceFileName;

        if (resourceSuffix.equals("mp4") || resourceSuffix.equals("mov")){
            // 保存视频资源
            fileUtils.writeFile(resource,resourcePath);
        }else{
            // 保存图片资源
            fileUtils.writeFile(resource,resourcePath);

            try {
                InputStream inputStream = resource.getInputStream();
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                int rota = img(resourcePath);
                if(rota == 0){
                    tResource.setResourceWidth(bufferedImage.getWidth()+"");
                    tResource.setResourceHeight(bufferedImage.getHeight()+"");
                }else{
                    tResource.setResourceWidth(bufferedImage.getHeight()+"");
                    tResource.setResourceHeight(bufferedImage.getWidth()+"");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 判断是否是上传封面
        if(tResource.getCoverType() == 1){
            if(resourceSuffix.equals("mp4") || resourceSuffix.equals("mov")){
                // 截取视频封面
                /*BufferedImage bufferedImage = ToolVideo.getScreenshot(resourcePath);*/

                VideoUtils videoUtils = new VideoUtils();
                BufferedImage bufferedImage = videoUtils.getCover(resourcePath);

                // 压缩图片
                // bufferedImage = compressUtils.commpressPicture(bufferedImage);
                // 保存图片封面
                fileUtils.writeFile(bufferedImage,coverPath);

                try {
                    Thumbnails.of(coverPath).size(376, 600).toFile(coverPath);
                } catch (IOException e) {
                    throw new RuntimeException("图片压缩出现异常："+ ExceptionUtil.stacktraceToString(e));
                }
            }else{
                // 压缩图片
                // BufferedImage bufferedImage = compressUtils.commpressPicture(resource);

                // 保存图片封面
                fileUtils.writeFile(resource,coverPath);
                // 在压缩
                try {
                    Thumbnails.of(coverPath).size(376, 600).toFile(coverPath);
                } catch (IOException e) {
                    throw new RuntimeException("图片压缩出现异常："+ ExceptionUtil.stacktraceToString(e));
                }
            }
        }else{
            String coverSuffix = fileUtils.getFileSuffix(cover);
            if (!coverSuffix.equals("jpg") && !coverSuffix.equals("png") &&
                !coverSuffix.equals("gif")){
                throw new RuntimeException("封面图片只支持jpg、png、gif格式！");
            }
            // 压缩图片
            /*BufferedImage bufferedImage = compressUtils.commpressPicture(cover);*/

            // 保存图片封面
            fileUtils.writeFile(cover,coverPath);

            // 在压缩
            try {
                Thumbnails.of(coverPath).size(376, 600).toFile(coverPath);
            } catch (IOException e) {
                throw new RuntimeException("图片压缩出现异常："+ ExceptionUtil.stacktraceToString(e));
            }
        }

        String userName = (String) request.getAttribute("userName");
        tResource.setCoverPath(coverFileName);
        tResource.setVisibleFlag(true);
        tResource.setCreateBy(userName);
        tResource.setCreateTime(new Date());
        tResource.setResourcePath(resourceFileName);
        tResource.setResourceType(resourceSuffix);
        this.save(tResource);
    }

    /**
     * 获取图片旋转度
     * 参考文章：https://blog.csdn.net/yuyu1067/article/details/116333935
     * @param args
     * @throws Exception
     */
    /*public static void main(String[] args)  throws Exception{
        File file = new File("C:\\wall\\b54c8baa-a4f7-4fc0-9cc1-c1b44f1e149c_cover.jpg");

        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if ("Orientation".equals(tag.getTagName())) {
                    String orientation = tag.getDescription();
                    if (orientation.contains("90")) {
                        System.out.println(90);
                    } else if (orientation.contains("180")) {
                        System.out.println(180);
                    } else if (orientation.contains("270")) {
                        System.out.println(270);
                    }
                }
            }
        }
        System.out.println(0);
    }*/


    public static int img(String path) throws Exception{
        Metadata metadata = ImageMetadataReader.readMetadata(new File(path));
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if ("Orientation".equals(tag.getTagName())) {
                    String orientation = tag.getDescription();
                    if (orientation.contains("90")) {
                        return 90;
                    } else if (orientation.contains("180")) {
                        return 180;
                    } else if (orientation.contains("270")) {
                        return 270;
                    }
                }
            }
        }
        return 0;
    }
}
