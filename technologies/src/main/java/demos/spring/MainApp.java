package demos.spring;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		ApplicationService obj =  ctx.getBean(ApplicationService.class);
		System.out.println(obj.getHello());
	}
}