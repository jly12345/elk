package com.luck.fun.luck.web;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.luck.fun.luck.config.Tenant;
import com.luck.fun.luck.entity.TError;
import com.luck.fun.luck.entity.TUserInfo;
import com.luck.fun.luck.service.TErrorService;
import com.luck.fun.luck.service.TUserInfoService;
import com.luck.fun.luck.web.util.HttpsUtils;
import com.luck.fun.luck.web.util.RedisUtils;
import com.luck.fun.luck.web.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Think
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WeChatController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    TUserInfoService userInfoService;

    String domain = "http://a2b61a4f.ngrok.io";

    @Autowired
    TErrorService errorService;
//    //家谱宝
//    private String appId="wx0e4dc3093c534acd";
//    private String secret="70833de87c2f525d9f994d3d0c0d4ddd";
//    private String token = "luck";

    //订阅号 ，无法使用
//    private String appId="wx5c710107c5c9b101";
//    private String secret="689f4e63e6e8d2d1896446f5b7f03654";
    /** 有效期：两者有效时间都是7200s。
     * 使用范围：通过网页授权获得的access_token，只能获取到对应的微信用户信息，与微信用户是一对一关系；而普通的access_token在有效期内可以使用，可以获取所有用户信息。
     * 次数限制：普通access_token每天获取最多次数为2000次，而网页授权的access_token获取次数没有限制。
     *
     * 网页授权获得的access_token
     */
    public String GETTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
    /**
     * 普通access_token
     */
    public String GET_ACCESS_TOKEN =  "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public String USERINFO = "" +
            "https://api.weixin.qq.com/sns/userinfo?access_token=TOKEN&openid=OPENID";


    @RequestMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String redirect_url = domain + "/wechat/game";
        //这里是回调的url
        redirect_url = URLEncoder.encode(redirect_url, "UTF-8");
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=APPID" +
                "&redirect_uri=REDIRECT_URI" +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=123#wechat_redirect";
        url = url.replace("APPID", Tenant.config.getAppid()).replace("REDIRECT_URI", redirect_url).replace("SCOPE", "snsapi_base");
        log.info(url);
        response.sendRedirect(url);
    }

    @RequestMapping("/check")
    @ResponseBody
    public String check(HttpServletRequest request){
        String signature = request.getParameter("signature");
        String echostr = request.getParameter("echostr");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        log.info("signature: "+ signature);
        log.info("echostr: "+ echostr);
        log.info("timestamp: "+ timestamp);
        log.info("nonce: "+ nonce);
        String sginStr = SignUtil.checkSignature(signature, timestamp, nonce,Tenant.config.getToken());
        log.info(sginStr);
        return echostr;
    }

    @RequestMapping("/game")
    public String game(String code, String state, RedirectAttributes model){
        //回调获得code，通过用户授权的code去获取微信令牌
        String url = GETTOKEN.replace("APPID",Tenant.config.getAppid()).replace("APPSECRET",Tenant.config.getSecret())
                .replace("CODE", code);
        log.info("url:{}" , url);
        String token = HttpsUtils.get(url);
        log.info("token:{}" , token);
        Map map = JSON.parseObject(token);
        //获取到了关键的令牌和openid后，就可以正式开始查询微信用户的信息，完成我们要做的微信绑定
        String access_token = (String) map.get("access_token");
        if(StringUtils.isEmpty(access_token)){
            errorService.save(new TError(map.toString()));
            return null;
        }
        String openid = (String) map.get("openid");
        String userInfo = HttpsUtils.get(USERINFO.replaceAll("TOKEN", access_token).replaceAll("OPENID", openid));
        log.info("userInfo:{}" , userInfo);
        TUserInfo now = userInfoService.getById(openid);
        if(now == null){
            TUserInfo user = new TUserInfo();
            user.setAppid(Tenant.config.getAppid());
            user.setOpenid(openid);
            user.setTenantNo(Tenant.config.getTenantNo());
            user.setUinfo(userInfo);
            userInfoService.save(user);
        }
        model.addFlashAttribute("openid",openid);
        return "redirect:/wechat/index";
    }

    @RequestMapping("/index")
    public ModelAndView index(@ModelAttribute("openid") String openid){
        log.info("openId:{}",openid);
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

}
