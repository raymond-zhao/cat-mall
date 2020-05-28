package edu.dlut.catmall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import edu.dlut.catmall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    // 在支付宝创建的应用的id 此处为沙箱测试的 APP_ID
    private   String app_id = "2016102100734722";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCa/eqKwpnlHlhuov/C/FXza2rSoN0rGopQOUR8H0znFZvBYFsjqDxkdVbqX4HllSuIGxfBB31Uuj3GPpa1Rp93QdKh2WOZRGNbNcjE1V5q263fjSE17qszdJ28lDQn7zmL6dn8ikYege3MJjbFiuEQ+2DjgAnw4ECVIzW0fEFUJNBjzycKHsqakyvsT99JeDsE1vqRdw6x8na5vMlx7FbIauCicdr72XNEShjSnQ3Bk2dU4LMQZt0eBo7HXCddgrBI2xwS6mh7mQRlBGfVhrGzsc87HyI3NIEzzxkyAJSlxdYm69BzLwAOYMX4kfNym2Hkg8eHoO/+OIGnOoQAKkJlAgMBAAECggEAHxSH8edwIOfi59y72Qfs3HFP7IjhkQEzdz7kotdL/pLwtuhxnkbgRWzaTwlz7Ovq3NJCAy1TTqhCNvzaD+PnmmySpm4HcParzeCNMBtxYZBH36pXXI9NuXXLI8O8ab2pFouAcQnxYbDAVM3c8eQVmegu17AXTjxJ9x4maNj56LtftY9S/PVNrTWc6ZwdBcWy6vAyg0uf5fV4cxRdxaX3Tmx71u9D7IsYrgRxYoNEWTx8rIymmjg3k1I0j1zWK8bqNbeoilAOzKh/J/sP7Ct9vPfjNNg2PbhKFVNMAL2on2OINzQIp5Byfv2/qp79CmFTF79ze2rPRpUme4N+QIPkSQKBgQDyhf7k4MAUO/wIFeki73qm0qxkzD4odYHBrHE1h+Mf3XFA4LA7W3PPg1kVTUXRTQtuEJcgXulRMbIvDCt/Uiq0puiMCkyXB0P6HcZCeZ2s1VlnLr7VbxJNmtAkOXi0HQ732QmR+e8tlgOU3mA9eiOY1RWzGOeEYnRw9WtBT1zUIwKBgQCjmr6tXxa+HMNvHx4ugPl5iRnr10H0HRcr3cnJ30nUOhrj1GVt/xYtpaXzh26OTGMUymXLwFtYMcxA2FfbuyUQTrvH+rxDM3CqbgC1mbXyCtgIbbUNv1GKZ+tpnQ+9X2AxMd4KCuVRrrUIHDURYX6TFMG9a4YjACezpbuXO0yT1wKBgEmKimWbJYO9QMqu4jg+yEGIBtC2Am9AWq2A/f9OinfiLXKP78DAFZkqajwEZ5R1OW8RxtIBFd0SJccQeKwuHVcUedXlVZ5CjMFuf+0udbqwI2Efkqyj9rfjpxQk/U4Yta5AeR9z26xGHiXpXOOngt3YV7Esbx/8vvR15yUxzKNjAoGAMVXx2DBLmGruGG1m2Zk1IgxRD8iq7+Vx3l2Ug1bdWa1n/HpJWAFLe9pvD+AwW6L3Ygai6e2I3jCuKoPTAsUNqTxE7kpTSWSSviJL/ndq4aGZgfkrVwjxmu45lHCzlWTUiiFClv6wJpCrsNVov2QKGmpw/iEVoyphzo/U4QcV0aMCgYEAsaMmuR0w7hsj0C6rhyUoUm2RwyBTYeUGfqYCl83jwfj8sEZWlkcBEbHzlo09xXYg794xZ2wbyW83agGY8D66wVT6RS2x5zDrEDBe5YZwRMDrlyPcM5JuF1+bylLumrwZXYyDn3krS0+Ef4dLtNNyrilbGoU1V+3IiUKuPsTNG44=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjHj48Hl0dvrdCLjMN8bl40Xvlse+lNGXW0SMR7BtyAvAza9IAeRte3GZdJ8/zNMOAeyD0hYBCMpGehXt/TH8YKcKP8txVOoYtrpVxEIT4lryoRZpo25nbhwdftIORe/413mphBhKUTWv9sdoqmOc1/v6JzJSXvyNh/u1PCOXzmIaRU01N92sLOzgZVpX0Mww7z7YkVS42sal1U90yvhUswdd9RkusMijhoyV3hii81Oemrh9ewEGlRPYHJVG+VO2FmPruLCRLOYmZfGcOOsnG790C019iNM88lMKM+DOJgIYILe/pPmRsDtUmJyDsPfB7g02kGQdqRttAZvbylwRwwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url = "http://6p1rgecslp.52http.net/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url = "http://member.catmall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\"1m\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
