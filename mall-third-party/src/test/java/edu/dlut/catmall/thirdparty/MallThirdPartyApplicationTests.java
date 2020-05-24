package edu.dlut.catmall.thirdparty;

import com.aliyun.oss.OSS;
import edu.dlut.catmall.thirdparty.component.SMSComponent;
import edu.dlut.catmall.thirdparty.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private OSS ossClient;

    @Resource
    private SMSComponent smsComponent;

    @Test
    public void sendSMS() {
        smsComponent.sendSMSCode("", "123456");
    }

    @Test
    public void smsTest() {
        String host = "https://smssend.shumaidata.com";
        String path = "/sms/send";
        String method = "POST";
        String appcode = "1fd04cc57b6c404cbbb45319889b875d";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("receive", "");
        querys.put("tag", "123456");
        querys.put("templateId", "M4F8845237");
        Map<String, String> bodys = new HashMap<>();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpload() throws FileNotFoundException {
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-beijing.aliyuncs.com";
//        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//        String accessKeyId = "LTAI4GGKDbWBT4SLvtN9EH2r";
//        String accessKeySecret = "Wb2Ya70Ym5x7PLmNC2PhQQj5ZehCT4";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 上传文件流。
        InputStream inputStream = new FileInputStream("/Users/raymond/Desktop/20200428.md");
        ossClient.putObject("catmall", "测试-1", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
