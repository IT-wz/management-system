import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class MailStart {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext act
                = new ClassPathXmlApplicationContext("applicationContext-mq-consumer.xml");
        act.start();

        System.in.read();
    }
}
