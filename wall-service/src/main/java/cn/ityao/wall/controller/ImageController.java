package cn.ityao.wall.controller;

import cn.ityao.wall.common.SuperController;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * byte-cabinet >>> 【com.cabinet.image.controller】
 * 资源文件
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
//@Controller
@Log4j2
//@RequestMapping("/static")
public class ImageController extends SuperController {

    /*@RequestMapping("/{path}")
    public void resources(@PathVariable String path, HttpServletResponse response, HttpServletRequest request) {
        String rootPath = itOptionService.getOption("saveFilePath");
        try {
            FileInputStream input = new FileInputStream(rootPath+path);
            byte[] data = new byte[input.available()];
            input.read(data);
            if(path.lastIndexOf(".jpg") != -1 || path.lastIndexOf(".png") != -1
                    || path.lastIndexOf(".gif") != -1){
                response.setContentType("image/jpeg");
            }else if(path.lastIndexOf(".mp4") != -1){
                response.setContentType("video/mp4");
            }else if(path.lastIndexOf(".mov") != -1){
                response.setContentType("video/quicktime");
            }
            response.getOutputStream().write(data);
            input.close();
        } catch (IOException e) {
            log.error("Wall后台出现代码异常："+e.getMessage());
        }
    }*/


}
