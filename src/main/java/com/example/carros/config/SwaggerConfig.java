package com.example.carros.config;

import java.util.Collections;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.globalOperationParameters(
                        Collections.singletonList(
                                new ParameterBuilder()
                                        .name("Authorization")
                                        .description("Bearer token")
                                        .modelRef(new ModelRef("string"))
                                        .parameterType("header")
                                        .required(false)
                                        .build())
                )
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.contact(new Contact("Ricardo Lecheta", "", ""))
				.title("Carros")
				.description("Documentação API dos Carros")
				.license("Apache Licence Version 2.0")
				.licenseUrl("https://apache.org")
				.version("1.0")
				.build();
	}
}
