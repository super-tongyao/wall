package cn.ityao.wall.controller;


import cn.ityao.wall.entity.TOption;
import cn.ityao.wall.entity.TResource;
import cn.ityao.wall.service.ITOptionService;
import cn.ityao.wall.util.DataResult;
import cn.ityao.wall.util.StringUtils;
import cn.ityao.wall.vo.TOptionVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 配置表 前端控制器
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@RestController
@RequestMapping("/t-option")
public class TOptionController {

    @Autowired
    private ITOptionService itOptionService;

    @PostMapping("/save")
    public DataResult save(@Valid @RequestBody TOptionVo tOptionVo){
        List<TOption> tOptionList = new ArrayList<>();


        String path = tOptionVo.getSaveFilePath();
        if (!path.substring(path.length()-1).equals("\\") && !path.substring(path.length()-1).equals("/")){
            path = tOptionVo.getSaveFilePath()+ File.separator;
        }
        TOption saveFilePath = new TOption();
        saveFilePath.setOptionKey("saveFilePath");
        saveFilePath.setOptionValue(path);
        tOptionList.add(saveFilePath);

        TOption beian = new TOption();
        beian.setOptionKey("beian");
        beian.setOptionValue(tOptionVo.getBeian());
        tOptionList.add(beian);

        TOption expireDate = new TOption();
        expireDate.setOptionKey("expireDate");
        expireDate.setOptionValue(tOptionVo.getExpireDate());
        tOptionList.add(expireDate);

        TOption secret = new TOption();
        secret.setOptionKey("secret");
        secret.setOptionValue(tOptionVo.getSecret());
        tOptionList.add(secret);

        TOption homeTitle = new TOption();
        homeTitle.setOptionKey("homeTitle");
        homeTitle.setOptionValue(tOptionVo.getHomeTitle());
        tOptionList.add(homeTitle);

        TOption initTagId = new TOption();
        initTagId.setOptionKey("initTagId");
        initTagId.setOptionValue(tOptionVo.getInitTagId());
        tOptionList.add(initTagId);

        itOptionService.updateBatchById(tOptionList);
        return DataResult.setSuccess(null);
    }

    @GetMapping("/query")
    public DataResult query(){
        TOptionVo tOptionVo = new TOptionVo();
        List<TOption> tOptionList =  itOptionService.list();
        for (TOption tOption : tOptionList){
            if(tOption.getOptionKey().equals("saveFilePath")){
                tOptionVo.setSaveFilePath(tOption.getOptionValue());
            }else if(tOption.getOptionKey().equals("beian")){
                tOptionVo.setBeian(tOption.getOptionValue());
            }else if(tOption.getOptionKey().equals("expireDate")){
                tOptionVo.setExpireDate(tOption.getOptionValue());
            }else if(tOption.getOptionKey().equals("secret")){
                tOptionVo.setSecret(tOption.getOptionValue());
            }else if(tOption.getOptionKey().equals("homeTitle")){
                tOptionVo.setHomeTitle(tOption.getOptionValue());
            }else if(tOption.getOptionKey().equals("initTagId")){
                tOptionVo.setInitTagId(tOption.getOptionValue());
            }
        }
        return DataResult.setSuccess(tOptionVo);
    }

    @GetMapping("/target")
    public DataResult target(){
        TOptionVo tOptionVo = new TOptionVo();
        List<TOption> tOptionList =  itOptionService.list();
        for (TOption tOption : tOptionList){
            if(tOption.getOptionKey().equals("beian")){
                tOptionVo.setBeian(tOption.getOptionValue());
            }else if(tOption.getOptionKey().equals("homeTitle")){
                tOptionVo.setHomeTitle(tOption.getOptionValue());
            }else if(tOption.getOptionKey().equals("initTagId")){
                tOptionVo.setInitTagId(tOption.getOptionValue());
            }
        }
        return DataResult.setSuccess(tOptionVo);
    }

}
