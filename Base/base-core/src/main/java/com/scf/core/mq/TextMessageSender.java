package com.scf.core.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang.StringUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;

import com.scf.core.exception.AppException;
import com.scf.core.exception.ExCode;
/**
 * mq消息发送器
 * @author wub
 */
public class TextMessageSender
{
    private JmsTemplate jmsTemplate;
    
    public void sendToTopic(String message, String topic)
    {
        if (StringUtils.isEmpty(topic))
        {
            throw new AppException(ExCode.MQ_001, "发送主题不能为空");
        }
        if (StringUtils.isEmpty(message))
        {
            throw new AppException(ExCode.MQ_000, "消息不能为空");
        }
        send(message, topic, true);
    }
    
    @Async
    public void sendToQueue(final String message, String queue)
    {
        if (StringUtils.isEmpty(queue))
        {
            throw new AppException(ExCode.MQ_001, "发送主题不能为空");
        }
        if (StringUtils.isEmpty(queue))
        {
            throw new AppException(ExCode.MQ_000, "消息不能为空");
        }
        send(message, queue, false);
    }
    
    @Async
    private void send(final String message, String name, boolean pubSubDomain)
    {
        Destination destination = null;
        if (pubSubDomain)
        {
            destination = new ActiveMQTopic(name);
        }
        else
        {
            destination = new ActiveMQQueue(name);
        }
        this.jmsTemplate.send(destination, new MessageCreator()
        {
            @Override
            public Message createMessage(Session session)
                throws JMSException
            {
                return session.createTextMessage(message);
            }
            
        });
    }
    
    public JmsTemplate getJmsTemplate()
    {
        return jmsTemplate;
    }
    
    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }
}
