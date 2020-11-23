import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class CompanyStart {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext act =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        act.start();

        System.in.read();
    }
}
