package com.itheima.mail.listener;

import com.alibaba.fastjson.JSONObject;
import com.itheima.utils.MailUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.Map;

public class MailListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //接收map, 解析map
        Map<String, String> map = JSONObject.parseObject(message.getBody(), Map.class);
        String to = map.get("to");
        String title = map.get("title");
        String content = map.get("content");

        System.out.println("接收到信息了,接收的邮箱为:" + to);

        //发送邮件
        MailUtil.sendMail(to, title, content);
    }
}
