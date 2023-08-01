package cn.ityao.wall.mapper;

import cn.ityao.wall.entity.TResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
public interface TResourceMapper extends BaseMapper<TResource> {

    List<TResource> selectAll(@Param("tagId") String tagId);
}
