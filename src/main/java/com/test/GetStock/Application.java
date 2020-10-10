package com.test.GetStock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;

@SpringBootApplication
@EnableSwagger2
public class Application {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(metadata());
	}

	private ApiInfo metadata() {
		return new ApiInfoBuilder().title("GetStock").description("미국의 기업코드로 180일 내에 최대 수익 계산").version("1.0").build();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
