package com.jinelei.bitterling.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bitterling项目API文档")
                        .description("Bitterling项目接口文档，包含所有业务接口")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("jinelei")
                                .email("jinelei@163.com")
                                .url("https://jinelei.com:9443"))
                        .license(new License()
                                .name("MIT许可证")
                                .url("https://opensource.org/licenses/MIT")));
    }
}