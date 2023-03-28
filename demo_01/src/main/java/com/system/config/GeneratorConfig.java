package com.system.config;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

public class GeneratorConfig {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/test", "root", "root")
                //全局配置
                .globalConfig(builder -> {
                    builder.author("author") // 设置作者
                            .disableOpenDir()   // 禁止打开输出目录
                            .outputDir("D:\\workspace\\demo\\demo_01\\src\\main\\java"); // 指定输出目录
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent("com.system") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\workspace\\demo\\demo_01\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.controllerBuilder().enableRestStyle();  // 开启生成@RestController控制器
                    builder.entityBuilder().enableLombok(); // 开启lombok
                    builder.serviceBuilder().formatServiceFileName("%sService");    // 设置文件名
                })
                .execute();
    }
}
