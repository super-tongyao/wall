package cn.ityao.wall.service;

import cn.ityao.wall.entity.TOption;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 配置表 服务类
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
public interface ITOptionService extends IService<TOption> {

    public String getOption(String optionKey);
}
