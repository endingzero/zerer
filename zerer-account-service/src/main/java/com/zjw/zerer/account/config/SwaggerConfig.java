package com.zjw.zerer.account.config;

import com.zjw.zerer.core.util.Result;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.service.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @ClassName: SwaggerConfig
 * @Description: swagger
 * @author: jiewei
 * @date: 2019/7/31
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Value("#{'${spring.cloud.client.hostname}'+':${server.port}'}")
    private String host;

    @Bean
    public Docket openApi() {
        return build("账户接口", "/account/.*");
    }


    private Docket build(String groupName, String paths) {
        return build(groupName, paths, true);
    }

    private Docket build(String groupName, String paths, boolean commonParam) {
        ArrayList<Parameter> list = new ArrayList<>();
//        if (commonParam) {
//            list = Lists.newArrayList(
//                    new ParameterBuilder().name("token").description("登陆token,公共参数").modelRef(new ModelRef("String"))
//                            .parameterType("query").required(true).build(),
//                    new ParameterBuilder().name("shopId").description("店铺ID,公共参数").modelRef(new ModelRef("String"))
//                            .parameterType("query").required(true).build());
//        }

        return new Docket(DocumentationType.SWAGGER_2).host(host).groupName(groupName)
                .genericModelSubstitutes(DeferredResult.class).genericModelSubstitutes(Result.class)
                .useDefaultResponseMessages(false).forCodeGeneration(false).pathMapping("/")
                .globalOperationParameters(list).select().paths(regex(paths))
                .build()
                .apiInfo(new ApiInfoBuilder().title(groupName)
                        .description("统一返回格式\n{\n \"code\": 0,\n \"data\": [],\n \"msg\": \"成功\"\n},data返回值请查看对应接口的Example Value")
                        .version("0.1").build());
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

}
