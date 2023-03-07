package cn.ityao.wall.controller;

/**
 * wall-service >>> 【cn.ityao.wall.controller】
 *
 * @author: tongyao
 * @since: 2023-03-06 17:16
 */
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;

/**
 * @description: 视频流和音频流加载设置
 * @return:
 * @author: Ming
 * @time: 2022/6/24
 */
@Component
public class VideoHttpRequestHandler extends ResourceHttpRequestHandler {
    public final static String ATTR_FILE = "NON-STATIC-FILE";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final Path filePath = (Path) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(filePath);
    }
}

