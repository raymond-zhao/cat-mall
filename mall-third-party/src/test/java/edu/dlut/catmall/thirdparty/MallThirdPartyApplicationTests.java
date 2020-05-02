package edu.dlut.catmall.thirdparty;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class MallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private OSS ossClient;

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
