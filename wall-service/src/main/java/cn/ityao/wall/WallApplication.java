package cn.ityao.wall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Wall启动类
 *
 * @author tongyao
 * @since 2023-02-14
 */
@SpringBootApplication
@MapperScan(basePackages = {
        "cn.ityao.wall.mapper"
})
public class WallApplication extends SpringBootServletInitializer {

    public static void main(String[] args){
        SpringApplication.run(WallApplication.class, args);
    }
}
