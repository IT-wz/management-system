package com.itheima.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发邮件工具类
 */
public final class MailUtil {

    //下面两项需要我们修改
    private static final String USER = "itheima_xm001@126.com"; // 发件人箱地址
    private static final String PASSWORD = "LGAGMGHTETRLUCRQ"; // 授权码

    /**
     * @param to      收件人邮箱
     * @param title   标题
     * @param content 邮件正文
     */
    /* 发送验证信息的邮件 */
    public static boolean sendMail(String to, String title, String content) {
        try {
            final Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "SMTP");//设置发邮件的协议
            props.setProperty("mail.host", "smtp.126.com");//设置发邮件的地址(smtp邮箱服务器地址)
            props.setProperty("mail.smtp.auth", "true");// 指定验证为true

            // 发件人的账号
            props.put("mail.user", USER);

            //发件人的密码
            props.put("mail.password", PASSWORD);

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            String username = props.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username);
            message.setFrom(form);
            // 设置收件人
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            // 设置邮件标题
            message.setSubject(title);
            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception { // 做测试用
        sendMail("3020838205@qq.com", "测试邮件，无需回复。", "你好，这是一封测试邮件");
        System.out.println("发送成功");
    }

}
