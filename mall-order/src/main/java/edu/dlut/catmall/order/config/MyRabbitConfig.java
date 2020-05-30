package edu.dlut.catmall.order.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/21  12:50
 * DESCRIPTION:
 **/
@Configuration
public class MyRabbitConfig {

    // @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 不手动创建构造器的话会产生循环引用
     * @param connectionFactory
     * @return
     */
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter(messageConverter());
        initRabbitTemplate();
        return rabbitTemplate;
    }

    // @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {

        });
        // 只要消息没有投递给指定的队列，就出发失败回调。
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {

        });
    }

}
