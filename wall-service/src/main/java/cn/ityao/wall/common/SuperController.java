package cn.ityao.wall.common;

import cn.ityao.wall.service.ITOptionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * wall-service >>> 【cn.ityao.wall.common】
 * 通用Controller类
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
public class SuperController {

    @Autowired
    public ITOptionService itOptionService;
}
