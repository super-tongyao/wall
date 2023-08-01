package cn.ityao.wall.controller;


import cn.ityao.wall.entity.TOption;
import cn.ityao.wall.entity.TResource;
import cn.ityao.wall.entity.TTag;
import cn.ityao.wall.service.ITOptionService;
import cn.ityao.wall.service.ITResourceService;
import cn.ityao.wall.service.ITTagService;
import cn.ityao.wall.util.DataResult;
import cn.ityao.wall.util.SecurityUtils;
import cn.ityao.wall.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@RestController
@RequestMapping("/t-tag")
public class TTagController {


    @Autowired
    private ITTagService itTagService;

    @Autowired
    private ITResourceService itResourceService;

    @Autowired
    private ITOptionService itOptionService;

    @PostMapping("/encryptPassword")
    public DataResult encryptPassword(@Valid @RequestBody TTag tTag, HttpServletRequest request) {
        String userName = (String) request.getAttribute("userName");
        tTag.setModifyBy(userName);
        tTag.setModifyTime(new Date());
        tTag.setTagPasswordBoolean(true);
        tTag.setTagPassword(SecurityUtils.encryptPassword(tTag.getTagPassword()));
        itTagService.updateById(tTag);
        return DataResult.setSuccess(null);
    }

    @PostMapping("/saveOrUpdate")
    public DataResult saveOrUpdate(@Valid @RequestBody TTag tTag, HttpServletRequest request) {
        String userName = (String) request.getAttribute("userName");
        if (tTag.getSort() == null) {
            tTag.setSort(0);
        }
        if (StringUtils.isNotBlank(tTag.getTagId())) {
            tTag.setModifyBy(userName);
            tTag.setModifyTime(new Date());
            itTagService.updateById(tTag);
        } else {
            tTag.setCreateBy(userName);
            tTag.setCreateTime(new Date());
            itTagService.save(tTag);
        }
        return DataResult.setSuccess(null);
    }

    @GetMapping("/list")
    public DataResult list(
            String tagName,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        LambdaQueryWrapper<TTag> tTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tagName)) {
            tTagLambdaQueryWrapper.like(TTag::getTagName, tagName);
        }
        tTagLambdaQueryWrapper.orderByAsc(TTag::getCreateBy, TTag::getSort);

        Page<TTag> page = new Page<>(pageNo, pageSize);
        IPage<TTag> iPage = itTagService.page(page, tTagLambdaQueryWrapper);
        return DataResult.setSuccess(iPage);
    }

    @DeleteMapping("/delete")
    public DataResult delete(String tagIds) {
        List<String> tagId = Arrays.asList(tagIds.split(","));

        LambdaQueryWrapper<TResource> tResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        for (String str : tagId) {
            tResourceLambdaQueryWrapper.like(TResource::getTagId, str);
        }
        List<TResource> tResourceList = itResourceService.list(tResourceLambdaQueryWrapper);
        for (int i = 0; i < tResourceList.size(); i++) {
            for (int j = 0; j < tagId.size(); j++) {
                tResourceList.get(i).setTagId(tResourceList.get(i).getTagId().replaceAll(tagId.get(j) + ",", ""));
            }
        }
        if (tResourceList.size() != 0) {
            itResourceService.updateBatchById(tResourceList);
        }

        Map map = new HashMap();
        map.put("option_key", "initTagId");
        List<TOption> tOptionList = (List<TOption>) itOptionService.listByMap(map);
        TOption tOption = tOptionList.get(0);
        if (tagIds.indexOf(tOption.getOptionValue()) != -1) {
            tOption = new TOption();
            tOption.setOptionKey("initTagId");
            tOption.setOptionValue("");
            itOptionService.updateById(tOption);
        }

        itTagService.removeByIds(tagId);
        return DataResult.setSuccess(null);
    }

    @GetMapping("/query")
    public DataResult query() {
        LambdaQueryWrapper<TTag> tTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tTagLambdaQueryWrapper.select(TTag::getTagId, TTag::getTagName,TTag::getTagPassword,TTag::getTagPasswordFlag);
        tTagLambdaQueryWrapper.orderByAsc(TTag::getCreateBy, TTag::getSort);
        return DataResult.setSuccess(itTagService.list(tTagLambdaQueryWrapper));
    }

}
