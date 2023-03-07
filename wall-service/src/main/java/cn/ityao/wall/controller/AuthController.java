package cn.ityao.wall.controller;

import cn.ityao.wall.entity.TUser;
import cn.ityao.wall.service.ITOptionService;
import cn.ityao.wall.service.ITUserService;
import cn.ityao.wall.util.DataResult;
import cn.ityao.wall.util.TokenUtil;
import cn.ityao.wall.util.encryption.DesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 *  前端登录
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@RestController
public class AuthController {

    @Autowired
    private ITUserService itUserService;

    @Autowired
    private ITOptionService itOptionService;

    @RequestMapping("/login")
    public DataResult login(String username,String password){
        TUser tUser = itUserService.loadUserByUsername(username);
        String pass = DesUtil.encryption(password);
        if(tUser == null){
            throw new RuntimeException("当前用户不存在");
        }else if(!tUser.getPassWord().equals(pass)){
            throw new RuntimeException("账号或密码错误！");
        }

        Integer expireDate = Integer.parseInt(itOptionService.getOption("expireDate"));
        String secret = itOptionService.getOption("secret");

        // 创建token
        String accessToken = TokenUtil.createToken(tUser.getUserId(), tUser.getUserName(), expireDate, secret);

        tUser.setAccess_token(accessToken);
        tUser.setLastTime(new Date());
        itUserService.updateById(tUser);
        return DataResult.setResult(tUser);
    }
}
