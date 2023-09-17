package com.ailearningvocabulary.bomnmi.web.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/27 22:22
 */
@Configuration
public class SwaggerConfiguration {

    //Create Docket
    @Bean
    public Docket docket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        //Create API info, the whole description about api document
        ApiInfo apiInfo = new ApiInfoBuilder().title("AI Help Learning Vocabulary In Context").version("1.0")
                .description("Combine Ai and vocabulary learning")
                .contact(new Contact("Mo Zhou", null, "bomnmi123@gmail.com"))
                .build();

        //Set and use ApiInfo
        docket = docket.apiInfo(apiInfo);

        //Set the package involving document generation
        docket = docket.select().apis(
                RequestHandlerSelectors.basePackage("com.ailearningvocabulary.bomnmi.web.controller"))
                .build();
        return docket;
    }
}
