package edu.dlut.catmall.order.listener;

import com.rabbitmq.client.Channel;
import edu.dlut.catmall.order.service.OrderService;
import edu.dlut.common.to.mq.SeckillOrderTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  23:46
 * DESCRIPTION:
 **/
@Slf4j
@Component
@RabbitListener(queues = {"order.seckill.order.queue"})
public class OrderSeckillListener {

    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void listener(SeckillOrderTO seckillOrder, Channel channel, Message message) throws IOException {
        try {
            log.info("准备创建秒杀订单的详细信息...");
            orderService.createSeckillOrder(seckillOrder);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
