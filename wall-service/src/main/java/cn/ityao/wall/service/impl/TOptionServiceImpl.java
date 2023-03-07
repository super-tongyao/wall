package cn.ityao.wall.service.impl;

import cn.ityao.wall.entity.TOption;
import cn.ityao.wall.mapper.TOptionMapper;
import cn.ityao.wall.service.ITOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配置表 服务实现类
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Service
public class TOptionServiceImpl extends ServiceImpl<TOptionMapper, TOption> implements ITOptionService {

    @Resource
    private TOptionMapper tOptionMapper;

    @Override
    public String getOption(String optionKey) {
        Map<String,Object> map = new HashMap();
        map.put("option_key",optionKey);
        List<TOption> tOptionList = tOptionMapper.selectByMap(map);
        if (tOptionList.size()==0){
            throw new RuntimeException("配置项："+optionKey+"，不存在数据库中！");
        }
        return tOptionList.get(0).getOptionValue();
    }
}
