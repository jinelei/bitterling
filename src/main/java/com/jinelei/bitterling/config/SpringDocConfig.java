package com.jinelei.bitterling.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 1. 定义登录请求体的 Schema
        Schema<?> loginSchema = new Schema<>()
                .type("object")
                .addProperty("username", new StringSchema().description("用户名"))
                .addProperty("password", new StringSchema().description("密码"));

        // 2. 定义请求体
        RequestBody requestBody = new RequestBody()
                .description("登录参数")
                .required(true)
                .content(new Content()
                        .addMediaType("application/x-www-form-urlencoded",
                                new MediaType().schema(loginSchema))
                        .addMediaType("application/json",
                                new MediaType().schema(loginSchema)));

        // 3. 定义响应
        ApiResponses apiResponses = new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("登录成功，跳转首页"))
                .addApiResponse("401", new ApiResponse().description("用户名或密码错误"));

        // 4. 定义 POST /login 接口
        Operation loginOperation = new Operation()
                .summary("用户登录")
                .description("Spring Security 默认登录接口")
                .requestBody(requestBody)
                .responses(apiResponses)
                .tags(Collections.singletonList("认证接口"));

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
                                .url("https://opensource.org/licenses/MIT")))
                .paths(new Paths().addPathItem("/login", new PathItem().post(loginOperation)));
    }
}
