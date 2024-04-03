package io.swagger.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

  private final DataSourceProperties dataSourceProperties;

  @Autowired
  public DataSourceConfiguration(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  @Bean
  @ConditionalOnProperty(name = "database.enabled", havingValue = "true", matchIfMissing = true)
  public DataSource dataSource() {
    return DataSourceBuilder.create()
        .url(dataSourceProperties.getUrl())
        .username(dataSourceProperties.getUsername())
        .password(dataSourceProperties.getPassword())
        .driverClassName(dataSourceProperties.getDriverClassName())
        .build();
  }
}
