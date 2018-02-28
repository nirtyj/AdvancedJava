package demos.spring;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RandomNumberGeneratorService {
	
	public Integer getRandomNumber()
	{
		Random r = new Random();
		return r.nextInt(20);
	}
}
