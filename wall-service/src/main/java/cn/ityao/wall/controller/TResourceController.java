package cn.ityao.wall.controller;


import cn.ityao.wall.common.SuperController;
import cn.ityao.wall.entity.TResource;
import cn.ityao.wall.entity.TTag;
import cn.ityao.wall.service.ITOptionService;
import cn.ityao.wall.service.ITResourceService;
import cn.ityao.wall.util.DataResult;
import cn.ityao.wall.util.FileUtils;
import cn.ityao.wall.util.StringUtils;
import cn.ityao.wall.util.picture.CompressUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@RestController
@RequestMapping("/t-resource")
public class TResourceController extends SuperController {

    @Autowired
    private ITResourceService itResourceService;

    @PostMapping("/upload")
    public DataResult upload(TResource tResource, MultipartFile[] resource,String[] title,HttpServletRequest request){
        if(resource == null){
            throw new RuntimeException("请上传图片或视频！");
        }
        itResourceService.uploadFileAndSave(tResource,resource,request);
        return DataResult.setSuccess(null);
    }

    @GetMapping("/query")
    public DataResult query(String tagId,
                            @RequestParam(defaultValue = "1") int pageNo,
                            @RequestParam(defaultValue = "10") int pageSize){

        LambdaQueryWrapper<TResource> tResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(tagId)){
            tResourceLambdaQueryWrapper.like(TResource::getTagId,tagId);
        }
        tResourceLambdaQueryWrapper.eq(TResource::isVisibleFlag,true);
        tResourceLambdaQueryWrapper.orderByDesc(TResource::getCreateTime);

        /*Page<TResource> page = new Page<>(pageNo,pageSize);
        IPage<TResource> iPage = itResourceService.page(page,tResourceLambdaQueryWrapper);*/

        String rootPath = itOptionService.getOption("saveFilePath");
        List<TResource> list = itResourceService.list(tResourceLambdaQueryWrapper);
        return DataResult.setSuccess(list);
    }

    @GetMapping("/list")
    public DataResult list(
            String title, String tagId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize){

        LambdaQueryWrapper<TResource> tResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(title)){
            tResourceLambdaQueryWrapper.like(TResource::getTitle,title);
        }
        if(StringUtils.isNotBlank(tagId)){
            tResourceLambdaQueryWrapper.like(TResource::getTagId,tagId);
        }
        tResourceLambdaQueryWrapper.orderByDesc(TResource::getCreateTime);

        Page<TResource> page = new Page<>(pageNo,pageSize);
        IPage<TResource> iPage = itResourceService.page(page,tResourceLambdaQueryWrapper);
        return DataResult.setSuccess(iPage);
    }

    @DeleteMapping("/delete")
    public DataResult delete(String resourceIds){
        List<String> resourceId = Arrays.asList(resourceIds.split(","));
        List<TResource> tResourceList = (List<TResource>) itResourceService.listByIds(resourceId);

        String saveFilePath = itOptionService.getOption("saveFilePath");
        File deleteFile = null;
        for (TResource tResource : tResourceList){
            deleteFile = new File(saveFilePath+tResource.getCoverPath());
            deleteFile.delete();

            deleteFile = new File(saveFilePath+tResource.getResourcePath());
            deleteFile.delete();
        }
        itResourceService.removeByIds(resourceId);
        return DataResult.setSuccess(null);
    }

    @PostMapping("/updateState")
    public DataResult updateState(String resourceId, boolean visibleFlag, HttpServletRequest request){
        String userName = (String) request.getAttribute("userName");

        TResource tResource = new TResource();
        tResource.setResourceId(resourceId);
        tResource.setVisibleFlag(visibleFlag);
        tResource.setModifyBy(userName);
        tResource.setModifyTime(new Date());
        itResourceService.updateById(tResource);
        return DataResult.setSuccess(null);
    }


    @PostMapping("/update")
    public DataResult update(String resourceId, String title,String tagIds, HttpServletRequest request){
        String userName = (String) request.getAttribute("userName");

        TResource tResource = new TResource();
        tResource.setResourceId(resourceId);
        tResource.setTitle(title);
        tResource.setTagId(tagIds);
        tResource.setModifyBy(userName);
        tResource.setModifyTime(new Date());
        itResourceService.updateById(tResource);
        return DataResult.setSuccess(null);
    }



}
