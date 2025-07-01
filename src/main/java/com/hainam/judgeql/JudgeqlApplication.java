package com.hainam.judgeql;

import com.hainam.judgeql.config.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JudgeqlApplication {

	public static void main(String[] args) {
		// Load environment variables from .env file
		DotenvLoader.load();
		
		SpringApplication.run(JudgeqlApplication.class, args);
	}

}
