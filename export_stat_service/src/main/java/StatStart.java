import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StatStart {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext act =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        act.start();

        System.in.read();
    }
}
