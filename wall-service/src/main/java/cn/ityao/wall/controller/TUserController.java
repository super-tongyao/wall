package cn.ityao.wall.controller;


import cn.ityao.wall.entity.TUser;
import cn.ityao.wall.service.ITUserService;
import cn.ityao.wall.util.DataResult;
import cn.ityao.wall.util.encryption.DesUtil;
import cn.ityao.wall.util.encryption.MD5Util;
import cn.ityao.wall.vo.RepassVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@RestController
@RequestMapping("/t-user")
public class TUserController {

    @Autowired
    private ITUserService itUserService;

    @RequestMapping("/query")
    public DataResult query(){
        return DataResult.setSuccess(itUserService.list());
    }

    @PostMapping(value = "/pass" , produces = {MediaType.APPLICATION_JSON_VALUE})
    public DataResult pass(@RequestBody RepassVo repassVo, HttpServletRequest request){
        if(repassVo.getNewPass() == null || "".equals(repassVo.getNewPass())){
            throw new RuntimeException("请输入新密码");
        }else if(repassVo.getReNewPass() == null || "".equals(repassVo.getReNewPass())){
            throw new RuntimeException("请再次输入新密码");
        }else if(!(repassVo.getNewPass().length() >= 8 && repassVo.getNewPass().length() <= 16)){
            throw new RuntimeException("密码长度要大于等于8位或小于等于16位");
        }else if(!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*", repassVo.getNewPass())){
            throw new RuntimeException("密码必须包含大小写字符和数字");
        }else if(!repassVo.getNewPass().equals(repassVo.getReNewPass())){
            throw new RuntimeException("两次密码输入不一致");
        }
        String userName = (String) request.getAttribute("userName");

        try {
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("user_name",userName);
            TUser tUser = null;
            try {
                List<TUser> list = (List<TUser>) itUserService.listByMap(tempMap);
                tUser = list.get(0);

            } catch (Exception e) {
                throw new RuntimeException("修改失败，账户不存在");
            }
            String oldPass = DesUtil.encryption(MD5Util.string2MD5(repassVo.getOldPass()));
            if(!tUser.getPassWord().equals(oldPass)){
                throw new RuntimeException("旧密码与原密码不一致");
            }
            tUser.setPassWord(DesUtil.encryption(MD5Util.string2MD5(repassVo.getNewPass())));
            itUserService.updateById(tUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return DataResult.setResult(null);
    }
}
