package com.jinelei.bitterling.config;

import com.jinelei.bitterling.domain.response.BookmarkResponse;
import com.jinelei.bitterling.domain.result.GenericResult;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
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
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
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

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        List<ResolvableType> resolvableTypeList = List.of(
//                ResolvableType.forClassWithGenerics(GenericResult.class, String.class),
//                ResolvableType.forClassWithGenerics(GenericResult.class, BookmarkResponse.class),
//                ResolvableType.forClassWithGenerics(GenericResult.class, ResolvableType.forClassWithGenerics(List.class, BookmarkResponse.class)),
//                ResolvableType.forClassWithGenerics(PageableResult.class, ResolvableType.forClassWithGenerics(List.class, BookmarkResponse.class)),
//                ResolvableType.forClassWithGenerics(GenericResult.class, MemoResponse.class),
//                ResolvableType.forClassWithGenerics(GenericResult.class, ResolvableType.forClassWithGenerics(List.class, MemoResponse.class)),
//                ResolvableType.forClassWithGenerics(PageableResult.class, ResolvableType.forClassWithGenerics(List.class, MemoResponse.class))
//        );
//        resolvableTypeList.stream()
//                .map(ResolvableType::getType)
//                .map(AnnotatedType::new)
//                .forEach(resolvableType -> ModelConverters.getInstance().resolveAsResolvedSchema(resolvableType));
//    }
//
//
//    // 缓存解析后的泛型 Schema（key：泛型类型标识，value：解析后的 Schema）
//    private final Map<String, Schema<?>> genericSchemaCache = new HashMap<>();
//
//    /**
//     * 步骤2：OpenAPI 文档生成时，将缓存的泛型 Schema 注入到文档中
//     */
//    @Bean
//    public OpenApiCustomizer genericSchemaCustomizer() {
//        return openApi -> {
//            // 获取 OpenAPI 文档的所有 Schema 组件
//            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
//            if (schemas == null) {
//                return;
//            }
//
//            // 2. 批量解析泛型类型，缓存解析后的 Schema
//            ModelConverters modelConverters = ModelConverters.getInstance();
//            // 1. 定义需要解析的所有泛型类型（修复嵌套泛型构建方式）
//            List<ResolvableType> genericTypeList = List.of(
//                    ResolvableType.forClassWithGenerics(GenericResult.class, String.class),
//                    ResolvableType.forClassWithGenerics(GenericResult.class, BookmarkResponse.class),
//                    ResolvableType.forClassWithGenerics(GenericResult.class, ResolvableType.forClassWithGenerics(List.class, BookmarkResponse.class)),
//                    ResolvableType.forClassWithGenerics(PageableResult.class, ResolvableType.forClassWithGenerics(List.class, BookmarkResponse.class)),
//                    ResolvableType.forClassWithGenerics(GenericResult.class, MemoResponse.class),
//                    ResolvableType.forClassWithGenerics(GenericResult.class, ResolvableType.forClassWithGenerics(List.class, MemoResponse.class)),
//                    ResolvableType.forClassWithGenerics(PageableResult.class, ResolvableType.forClassWithGenerics(List.class, MemoResponse.class))
//            );
//            for (ResolvableType type : genericTypeList) {
//                ResolvedSchema resolvedSchema = modelConverters.resolveAsResolvedSchema(new AnnotatedType(type.getType()));
//                if (resolvedSchema.schema != null) {
//                    // 用类型名称作为缓存 key（也可自定义唯一标识）
//                    genericSchemaCache.put(type.getType().getTypeName(), resolvedSchema.schema);
//                }
//            }
//            // 遍历缓存的泛型 Schema，替换/补充到文档中
//            for (Map.Entry<String, Schema<?>> entry : genericSchemaCache.entrySet()) {
//                String typeName = entry.getKey();
//                Schema<?> schema = entry.getValue();
//
//                // 核心：根据泛型类型名称，更新对应的 Schema（关键修复）
//                // 示例：处理 GenericResult 相关泛型
//                if (typeName.contains("GenericResult")) {
//                    updateGenericResultSchema(schemas, schema);
//                }
//                // 处理 PageableResult 相关泛型
//                else if (typeName.contains("PageableResult")) {
//                    updatePageableResultSchema(schemas, schema);
//                }
//            }
//        };
//    }
//
//    /**
//     * 辅助方法：更新 GenericResult 的 Schema，替换 data 字段为具体泛型类型
//     */
//    private void updateGenericResultSchema(Map<String, Schema> schemas, Schema<?> resolvedSchema) {
//        // 获取文档中默认生成的 GenericResult Schema
//        Schema<?> genericResultSchema = schemas.get("GenericResult");
//        if (genericResultSchema == null || resolvedSchema.getProperties() == null) {
//            return;
//        }
//
//        // 获取解析后的 data 字段（带具体泛型类型）
//        Schema<?> dataSchema = resolvedSchema.getProperties().get("data");
//        if (dataSchema != null) {
//            // 替换默认的 data 字段为具体泛型类型
//            genericResultSchema.getProperties().put("data", dataSchema);
//        }
//    }
//
//    /**
//     * 辅助方法：更新 PageableResult 的 Schema
//     */
//    private void updatePageableResultSchema(Map<String, Schema> schemas, Schema<?> resolvedSchema) {
//        Schema<?> pageableResultSchema = schemas.get("PageableResult");
//        if (pageableResultSchema == null || resolvedSchema.getProperties() == null) {
//            return;
//        }
//
//        // 替换 PageableResult 的 data 字段（假设 PageableResult 有 data 字段）
//        Schema<?> dataSchema = resolvedSchema.getProperties().get("data");
//        if (dataSchema != null) {
//            pageableResultSchema.getProperties().put("data", dataSchema);
//        }
//    }

    /**
     * 适配 2.8.15 版本：全局解析 GenericResult 泛型（替代废弃的 addAnnotatedType）
     */
    @Bean
    public OpenApiCustomizer genericResultCustomizer() {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            if (schemas == null) return;

            // 获取 GenericResult 的基础 Schema
            Schema<?> genericResultSchema = schemas.get("GenericResult");
            if (genericResultSchema == null || genericResultSchema.getProperties() == null) return;

            // ========== 1. 解析单层泛型：GenericResult<BookmarkResponse> ==========
            Type singleType = ResolvableType.forClassWithGenerics(GenericResult.class, BookmarkResponse.class).getType();
            updateDataSchema(genericResultSchema, singleType);

            // ========== 2. 解析多层泛型：GenericResult<List<BookmarkResponse>> ==========
            Type listType = ResolvableType.forClassWithGenerics(GenericResult.class, ResolvableType.forClassWithGenerics(List.class, BookmarkResponse.class)).getType();
            updateDataSchema(genericResultSchema, listType);

            // ========== 3. 解析基础类型泛型：GenericResult<String> ==========
            Type stringType = ResolvableType.forClassWithGenerics(GenericResult.class, String.class).getType();
            updateDataSchema(genericResultSchema, stringType);
        };
    }

    /**
     * 通用方法：更新 GenericResult 的 data 字段为具体泛型类型
     */
    private void updateDataSchema(Schema<?> genericResultSchema, Type genericType) {
        // 解析泛型类型的完整 Schema
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(genericType));

        // 提取 data 字段并替换
        if (resolvedSchema.schema != null && resolvedSchema.schema.getProperties() != null) {
            if (resolvedSchema.schema.getProperties() instanceof Map map) {
                Schema<?> dataSchema = (Schema<?>) map.get("data");
                if (dataSchema != null) {
                    genericResultSchema.getProperties().put("data", dataSchema);
                }
            }
        }
    }

}
