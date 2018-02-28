package demos.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public ApplicationService getService()
	{
		return new ApplicationService();
	}
	
	@Bean
	public RandomNumberGeneratorService getRandomNumberService()
	{
		return new RandomNumberGeneratorService();
	}
	
}
