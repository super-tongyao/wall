package cn.ityao.wall.config;

import cn.ityao.wall.service.ITOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * wall-service >>> 【cn.ityao.wall.config】
 * 本地图片资源映射
 *
 * @author: tongyao
 * @since: 2023-03-06
 */
@Configuration
public class ImageConfig implements WebMvcConfigurer {

    @Autowired
    public ITOptionService itOptionService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rootPath = itOptionService.getOption("saveFilePath");

        // 判断目录是否存在，不在就创建
        File file = new File(rootPath);
        if(!file.exists()){
            file.mkdirs();
        }
        registry.addResourceHandler("/static/**").addResourceLocations("file:"+rootPath);
    }

}
