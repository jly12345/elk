package com.luck.fun.luck.web;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luck.fun.luck.web.util.OKHttpUtil;
import com.luck.fun.luck.web.util.RedisUtils;
import com.luck.fun.luck.web.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Think
 */
@RestController
@RequestMapping("/wechat")
@Slf4j
public class WeChatController {

    @Autowired
    private RedisUtils redisUtils;
    //测试号
    private String appId="wxdba63f4cfbc5a7e5";
    private String secret="8bd74339ad982d840fa88f89a8d0b12e";

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
        String redirect_url ="http://19ce02ab.ngrok.io/wechat/test";
        //这里是回调的url
        String redirect_uri = URLEncoder.encode(redirect_url, "UTF-8");
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=APPID" +
                "&redirect_uri=REDIRECT_URI" +
                "&response_type=code" +
                "&scope=SCOPE" +
                "&state=123#wechat_redirect";
        url = url.replace("APPID", appId).replace("REDIRECT_URI", redirect_url).replace("SCOPE", "snsapi_base");
        log.info(url);
        response.sendRedirect(url);
    }

    @RequestMapping("/check")
    public String check(HttpServletRequest request){
        String signature = request.getParameter("signature");
        String echostr = request.getParameter("echostr");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        log.info("signature: "+ signature);
        log.info("echostr: "+ echostr);
        log.info("timestamp: "+ timestamp);
        log.info("nonce: "+ nonce);
        String sginStr = SignUtil.checkSignature(signature, timestamp, nonce);
        log.info(sginStr);
        return echostr;
    }

    @RequestMapping("/test")
    public JSONObject test(String code, String state){
        //回调获得code，通过用户授权的code去获取微信令牌
        String url = GETTOKEN.replace("APPID",appId).replace("APPSECRET",secret)
                .replace("CODE", code);
        log.info("url:"+url);
        String token = OKHttpUtil.httpGet(url);
        Map map = JSON.parseObject(token);
        //获取到了关键的令牌和openid后，
        //就可以正式开始查询微信用户的信息，完成我们要做的微信绑定
        String access_token = (String) map.get("access_token");
        if(StringUtils.isEmpty(access_token)){
            log.info("网页授权信息:"+token);
            return null;
        }
        String openid = (String) map.get("openid");
        String userInfo = OKHttpUtil.httpGet(USERINFO.replaceAll("TOKEN", access_token).replaceAll("OPENID", openid));
        log.info("userInfo:"+userInfo);
        return JSON.parseObject(userInfo);
    }
}
