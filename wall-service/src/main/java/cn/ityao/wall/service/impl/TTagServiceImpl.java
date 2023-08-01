package cn.ityao.wall.service.impl;

import cn.ityao.wall.entity.TTag;
import cn.ityao.wall.mapper.TTagMapper;
import cn.ityao.wall.service.ITTagService;
import cn.ityao.wall.util.DataResult;
import cn.ityao.wall.util.SecurityUtils;
import cn.ityao.wall.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Service
public class TTagServiceImpl extends ServiceImpl<TTagMapper, TTag> implements ITTagService {

    @Autowired
    private TTagMapper tTagMapper;

    /**
     * 判断密码是否一致
     * @param tagId
     * @param tagPassword
     * @return
     */
    public Boolean matchesPassword(String tagId, String tagPassword) {
        boolean flag = false;
        if (StringUtils.isNotBlank(tagId)) {
            TTag detailTag = tTagMapper.selectById(tagId);
            String encodedPassword = detailTag.getTagPassword();
            flag = SecurityUtils.matchesPassword(tagPassword, encodedPassword);
        }
        return flag;
    }
}
