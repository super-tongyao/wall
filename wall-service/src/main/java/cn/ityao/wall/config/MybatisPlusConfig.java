package cn.ityao.wall.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * wall-service >>> 【cn.ityao.wall.config】
 * MyBatisPlus分页配置
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        return page;
    }
}
