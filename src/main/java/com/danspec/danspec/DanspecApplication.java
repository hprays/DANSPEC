package com.danspec.danspec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // 이 줄이 추가됨

@EnableJpaAuditing // 스위치를 켜야 날짜가 자동으로 찍힙
@SpringBootApplication
public class DanspecApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanspecApplication.class, args);
	}

}
