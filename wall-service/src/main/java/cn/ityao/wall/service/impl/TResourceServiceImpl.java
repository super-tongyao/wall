package cn.ityao.wall.service.impl;

import cn.ityao.wall.entity.TResource;
import cn.ityao.wall.mapper.TResourceMapper;
import cn.ityao.wall.service.ITOptionService;
import cn.ityao.wall.service.ITResourceService;
import cn.ityao.wall.util.FileUtils;
import cn.ityao.wall.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    private String resourceSuffix = "";
    private String resourceFileName = "";
    private String resourcePath = "";

    @Override
    public void uploadFileAndSave(TResource tResource, MultipartFile[] resource, HttpServletRequest request){
        String saveFilePath = itOptionService.getOption("saveFilePath");

        List<String> titles = new ArrayList<>();
        List<Map<String,String>> bedUrl = null;

        // 只验证类型为1的本地上传资源类型包括视频类型
        if (tResource.getSourceType().equals("1")){
            localUploadSubffix(resource);
            // 处理资源标题
            if (!tResource.getTitle().equals("")){
                String[] title = tResource.getTitle().split(",");
                titles = Arrays.asList(title);
            }
        }else if(tResource.getSourceType().equals("2")){
            // 此处不会验证图床URL
            bedUrl = (List<Map<String,String>>) JSONObject.parse(tResource.getBedUrl());
            // 处理图床标题
            for (int i = 0; i < bedUrl.size(); i++) {
                titles.add(bedUrl.get(i).get("title"));
            }
        }else{
            throw new RuntimeException("未知来源格式！");
        }

        // 开始保存处理数据
        try {
            for (int i = 0; i < titles.size(); i++) {
                // 统一声明好封面和资源的文件名
                String uuid = StringUtils.getUUID();

                // 封面文件名
                String coverFileName = uuid + "_cover.jpg";

                if (tResource.getSourceType().equals("1")){
                    // 获取资源后缀
                    resourceSuffix = fileUtils.getFileSuffix(resource[i].getOriginalFilename());

                    // 资源文件名
                    resourceFileName = uuid + "." + resourceSuffix;

                    // 资源完整的存储路径
                    resourcePath = saveFilePath + resourceFileName;

                    // 保存视频 或者 图片资源
                    fileUtils.writeFile(resource[i].getInputStream(),resourcePath);

                    tResource.setResourcePath(resourceFileName);
                }else{
                    // - -
                    resourcePath = bedUrl.get(i).get("url");
                    resourceFileName = resourcePath;
                }

                // 封面完整的存储路径
                String coverPath = saveFilePath + coverFileName;

                // 获取视频 或者 图片资源 封面
                fileUtils.writeCover(resourcePath,coverPath);

                // 压缩封面
                Thumbnails.of(coverPath).scale(1).toFile(coverPath);

                // 保存
                String userName = (String) request.getAttribute("userName");
                tResource.setResourceId(null);
                tResource.setTitle(titles.get(i));
                tResource.setCoverPath(coverFileName);
                tResource.setVisibleFlag(true);
                tResource.setCreateBy(userName);
                tResource.setCreateTime(new Date());
                tResource.setResourcePath(resourcePath);
                this.save(tResource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
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
