package cn.ityao.wall.service;

import cn.ityao.wall.entity.TResource;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
public interface ITResourceService extends IService<TResource> {

    public void uploadFileAndSave(TResource tResource, MultipartFile[] resource, HttpServletRequest request);
}
