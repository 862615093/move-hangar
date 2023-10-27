package snegrid.move.hangar.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;

/**
 * @author wangwei
 */
@Data
@Configuration
@EnableSwagger2
@EnableKnife4j
@ConfigurationProperties(prefix = "spring.application")
public class SwaggerConf {

    private String name;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        String version = "1.0.0";
        Long timestamp = System.currentTimeMillis();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description(name.toUpperCase() + " 服务 RESTFUL APIS")
                        .version(version + "_" + format.format(timestamp))
                        .build())
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("snegrid.move.hangar"))
                .paths(PathSelectors.any())
                .build();
    }
}
