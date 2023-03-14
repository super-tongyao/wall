package cn.ityao.wall.config;

import cn.ityao.wall.exception.Unauthorizedxception;
import cn.ityao.wall.service.ITOptionService;
import cn.ityao.wall.util.TokenUtil;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * wall-service >>> 【cn.ityao.wall.config】
 * JWT 拦截器
 *
 * @author: tongyao
 * @since: 2023-02-21
 */
public class JwtInterceptor implements HandlerInterceptor{

    @Autowired
    private ITOptionService itOptionService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("access_token");

        //tonken验证
        if (token == null || "null".equals(token)) {
            throw new Unauthorizedxception(401,"请先登录！");
        }

        String secret = itOptionService.getOption("secret");
        // 验证令牌正确性
        Map<String, Claim> userData = null;
        try {
            userData = TokenUtil.parsingToken(token,secret);
        } catch (Exception e) {
            throw new Unauthorizedxception(401,"Token令牌不正确，请先登录。");
        }
        String userId = userData.get("userId").asString();
        String userName = userData.get("userName").asString();

        httpServletRequest.setAttribute("userId",userId);
        httpServletRequest.setAttribute("userName",userName);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }




}
