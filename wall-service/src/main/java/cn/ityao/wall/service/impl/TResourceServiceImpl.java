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
import com.alibaba.fastjson.JSONObject;
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
import java.util.*;

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

    private FileUtils fileUtils = new FileUtils();

    @Override
    public void uploadFileAndSave(TResource tResource, MultipartFile[] resource, HttpServletRequest request){
        String saveFilePath = itOptionService.getOption("saveFilePath");

        // 只验证类型为1的本地上传资源类型包括视频类型
        if (tResource.getSourceType().equals("1")){
            localUploadSubffix(resource);
        }else if(tResource.getSourceType().equals("2")){

        }else{
            throw new RuntimeException("未知来源格式！");
        }

        // 此处不会验证图床URL
        /*List<Map<String,String>> bedUrl = (List<Map<String,String>>) JSONObject.parse(tResource.getBedUrl());
        for (int i = 0; i < bedUrl.size(); i++) {
            System.out.println(bedUrl.get(i).get("url"));
        }*/


        List<String> stringList = new ArrayList<>();
        if (!tResource.getTitle().equals("")){
            String[] str = tResource.getTitle().split(",");
            stringList = Arrays.asList(str);
        }
        try {
            for (int i = 0; i < resource.length; i++) {

                // 统一声明好封面和资源的文件名
                String uuid = StringUtils.getUUID();

                // 封面文件名
                String coverFileName = uuid +"_cover.jpg";

                // 获取资源后缀
                String resourceSuffix = fileUtils.getFileSuffix(resource[i].getOriginalFilename());

                // 资源文件名
                String resourceFileName = uuid + "." + resourceSuffix;

                // 封面完整的存储路径
                String coverPath = saveFilePath + coverFileName;
                // 资源完整的存储路径
                String resourcePath = saveFilePath + resourceFileName;

                // 保存视频 或者 图片资源
                fileUtils.writeFile(resource[i].getInputStream(),resourcePath);

                // 获取视频 或者 图片资源 封面
                fileUtils.writeCover(resourcePath,coverPath);

                // 压缩封面
                Thumbnails.of(resourcePath).scale(1).toFile(coverPath);

                // 保存
                String userName = (String) request.getAttribute("userName");
                tResource.setCoverPath(coverFileName);
                tResource.setVisibleFlag(true);
                tResource.setCreateBy(userName);
                tResource.setCreateTime(new Date());
                tResource.setResourcePath(resourceFileName);
                tResource.setResourceType(resourceSuffix);
                this.save(tResource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * 验证本地上传图片或视频后缀
     * @param resource
     */
    public void localUploadSubffix(MultipartFile[] resource){
        boolean subffix = false;
        for (int i = 0; i < resource.length; i++) {
            // 资源后缀
            String resourceSuffix = fileUtils.getFileSuffix(resource[i].getOriginalFilename());
            if (resourceSuffix.equals("jpeg")){
                resourceSuffix = "jpg";
            }
            if (!resourceSuffix.equals("jpg") && !resourceSuffix.equals("png") &&
                    !resourceSuffix.equals("gif") && !resourceSuffix.equals("mp4") &&
                    !resourceSuffix.equals("mov") && !resourceSuffix.equals("wmv")){
                subffix = true;
                break;
            }
        }
        if (subffix){
            throw new RuntimeException("图片或视频只支持jpg、png、gif、mp4、mov、wmv格式！");
        }
    }
}
