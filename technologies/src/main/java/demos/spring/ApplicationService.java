package demos.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

	@Autowired
	RandomNumberGeneratorService randomNumberService;

	public String getHello() {
		StringBuilder sb = new StringBuilder();
		Integer random = randomNumberService.getRandomNumber();
		sb.append("Printing Hello - " + random + " times\n");
		for(int i = 1; i<= random; i++)
		{
			sb.append("hello\n");
		}
		return sb.toString();
	}
}
