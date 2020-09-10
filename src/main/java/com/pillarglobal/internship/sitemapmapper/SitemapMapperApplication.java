package com.pillarglobal.internship.sitemapmapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableScheduling
@SpringBootApplication
public class SitemapMapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SitemapMapperApplication.class, args);
	}

	@Bean
	public XmlMapper getXmlMapper(){
		return new XmlMapper();
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(3);
		return threadPoolTaskScheduler;
	}


}
