package com.vz.esap.producer.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket restfulApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("kafkatest-api").select().paths(paths()).build()
				.apiInfo(apiInfo());
	}

	private Predicate<String> paths() {
		return or(regex("/kafkatest.*"));
	}

	private ApiInfo apiInfo() {

		ApiInfo apiInfo = null;
		apiInfo = new ApiInfo("ESVRRS Kafka Test Producer", "ESVRRS Kafka Test services: LaaS", "1.0",
				"http://oneconfluence.verizon.com", "kalyan.sarwa@verizon.com", "Licence",
				"http://oneconfluence.verizon.com/display/ESVRRS/KafkaServices");
		return apiInfo;
	}
}
