package com.luck.fun.luck;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {

    public static final String DB_URL = "jdbc:mysql://192.168.137.101:3306/luck?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "123456";
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String AUTHOR = "lingyun.jiang";
    //生成的文件输出到哪个目录
    public static final String OUTPUT_FILE = "D:\\interest";
    //包名，会按照com/example/demo这种形式生成类
    public static final String PACKAGE = "com.luck.fun.luck";
    //TODO 更多配置请参考http://mp.baomidou.com/#/generate-code

    public static void main(String[] args) {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = DB_URL;
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(USER_NAME)
                .setPassword(PASSWORD)
                .setDriverName(DRIVER);
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setNaming(NamingStrategy.underline_to_camel);
        config.setActiveRecord(false)
                .setEnableCache(false)
                .setAuthor(AUTHOR)
                .setOutputDir(OUTPUT_FILE)
                .setFileOverride(true)
                .setServiceName("%sService");
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(PACKAGE)
                                .setController("web")
                                .setEntity("entity")
                ).execute();
    }


}