package cn.ityao.wall.service;

import cn.ityao.wall.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
public interface ITUserService extends IService<TUser> {

    public TUser loadUserByUsername(String userName);
}
