package cn.ityao.wall.mapper;

import cn.ityao.wall.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
public interface TUserMapper extends BaseMapper<TUser> {

    public TUser loadUserByUsername(@Param("userName") String userName);

}
