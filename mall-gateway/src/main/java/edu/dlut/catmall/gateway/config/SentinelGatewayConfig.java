package edu.dlut.catmall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import edu.dlut.common.exception.BizCodeEnum;
import edu.dlut.common.utils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/30  16:20
 * DESCRIPTION:
 **/
@Configuration
public class SentinelGatewayConfig {

    public SentinelGatewayConfig() {
        GatewayCallbackManager.setBlockHandler(((exchange, t) -> {
            R error = R.error(BizCodeEnum.TOO_MANY_REQUESTS.getCode(), BizCodeEnum.TOO_MANY_REQUESTS.getMsg());
            String jsonString = JSON.toJSONString(error);
            // Mono<ServerResponse> body = ServerResponse.ok().body(Mono.just(jsonString), String.class);
            return ServerResponse.ok().body(Mono.just(jsonString), String.class);
        }));
    }

}
