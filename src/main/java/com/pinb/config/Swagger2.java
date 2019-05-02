package com.pinb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
//@EnableSwagger2
public class Swagger2 {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.pinb.control")).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("拼吧-在线api接口文档，支持直接调用测试").description(
				"1、api基础地址：<br>(测试环境)https://apitest.pinb.vip/pinb-service<br>(产线环境)https://api.pinb.vip/pinb-service<br>"
						+ "2、公共请求参数：sign,userWxUnionid,userWxOpenid,page,rows(page,rows,分页list数据公参)；请求示例：{'sign':'','userWxUnionid':'sx2345fd23cc4','userWxOpenid':'sx2345fd23cc4',}<br>"
						+ "3、响应报文格式规约：{'retCode':10000,'retMsg':'操作成功','data':{'userName':'用户姓名','userPhone':'18516369668'}}<br>"
						+ "4、公共响应code：(\"10000\", \"操作成功\")," + "(\"11111\", \"操作失败,%s\"),"
						+ "(\"10001\", \"参数[%s]非法,%s\")," + "(\"10002\", \"必要参数[%s]残缺\"),"
						+ "(\"10005\", \"外部服务请求超时\")," + "(\"10006\", \"外部服务请求异常,%s\"),")
				.termsOfServiceUrl("").contact("pcbaby-ch@qq.com").version("1.0").build();
	}

}
