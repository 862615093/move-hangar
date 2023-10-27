package snegrid.move.hangar;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.HashMap;

/**
 * @author weiwang127
 */
public class MybatisPlusAutoGenerator {
    public static void main(String[] args) {
        String basePath = "D:\\work-space\\company-project\\move-hangar\\src\\main\\java\\";
        FastAutoGenerator.create(
                "jdbc:mysql://47.99.163.33:3306/move_hangar?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
                "root",
                "123456")
                .globalConfig(builder -> {
                    builder.author("wangwei") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(basePath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("snegrid.move.hangar.business") // 设置父包名
//                            .moduleName("zljs") // 设置父包模块名
                            .pathInfo(new HashMap<OutputFile, String>() {{
                                put(OutputFile.controller, basePath + "snegrid/move/hangar/business/controller");
                                put(OutputFile.entity, basePath + "snegrid/move/hangar/business/domain/entity");
                                put(OutputFile.service, basePath + "snegrid/move/hangar/business/service");
                                put(OutputFile.serviceImpl, basePath + "snegrid/move/hangar/business/service/impl");
                                put(OutputFile.mapper, basePath + "snegrid/move/hangar/business/mapper");
                                put(OutputFile.xml, "D:\\work-space\\company-project\\move-hangar\\src\\main\\" + "resources/mapper/business");
                            }}); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude("t_device", "t_file", "t_route", "t_fly_task") // 设置需要生成的表名
                            .addTablePrefix("t_")
                            .entityBuilder().enableLombok();
                })
                .templateEngine(new VelocityTemplateEngine()) // 默认的是Velocity引擎模板
                .execute();
    }
}
