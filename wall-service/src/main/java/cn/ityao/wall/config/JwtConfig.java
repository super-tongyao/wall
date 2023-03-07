package cn.ityao.wall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * wall-service >>> 【cn.ityao.wall.config】
 * JWT 配置文件
 *
 * @author: tongyao
 * @since: 2023-02-21
 */
@Configuration
public class JwtConfig implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(jwtInterceptor());

        registration.excludePathPatterns(
                "/favicon.ico",
                "/login",
                "/t-tag/query",
                "/t-resource/query",
                "/t-option/target",
                "/static/**");
        registration.addPathPatterns("/**");
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
