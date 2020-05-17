package edu.dlut.catmall.thirdparty.component;

import edu.dlut.catmall.thirdparty.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  16:54
 * DESCRIPTION:
 **/
@Component
public class SMSComponent {

    public void sendSMSCode(String phone, String code) {
        String host = "https://zwp.market.alicloudapi.com";
        String path = "/sms/sendv2";
        String method = "GET";
        String appcode = "1fd04cc57b6c404cbbb45319889b875d";
        Map<String, String> headers = new HashMap<>();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> query = new HashMap<>();
        query.put("content", "【云通知】您的验证码是"+ code + "。如非本人操作，请忽略本短信");
        query.put("mobile", phone);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, query);
            System.out.println(response.toString());
            // 获取response的body
            // System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
