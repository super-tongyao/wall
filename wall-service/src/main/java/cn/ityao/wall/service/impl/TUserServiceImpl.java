package cn.ityao.wall.service.impl;

import cn.ityao.wall.entity.TUser;
import cn.ityao.wall.mapper.TUserMapper;
import cn.ityao.wall.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public TUser loadUserByUsername(String userName) {
        return tUserMapper.loadUserByUsername(userName);
    }
}
