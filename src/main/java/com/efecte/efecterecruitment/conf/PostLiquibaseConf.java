package com.efecte.efecterecruitment.conf;

import liquibase.integration.spring.MultiTenantSpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PostLiquibaseConf {
    @Value("${spring.liquibase.liquibase-schema:db_changelog}")
    private String schema;

    @Bean
    public MultiTenantSpringLiquibase postLiquibase(DataSource dataSource) {
        var moduleConfig = new MultiTenantSpringLiquibase();
        moduleConfig.setDataSource(dataSource);
        moduleConfig.setDefaultSchema("posts");
        moduleConfig.setLiquibaseSchema(schema);
        moduleConfig.setChangeLog("db/posts/changelog/postgres/db-changelog.xml");
        return moduleConfig;
    }
}
