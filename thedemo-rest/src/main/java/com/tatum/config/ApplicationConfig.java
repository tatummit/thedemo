package com.tatum.config;

import com.tatum.properties.ApplicationProperties;
import com.tatum.properties.GoogleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.tatum.exception.DemoRuntimeException;
import com.tatum.factory.config.BinaryManagerConfig;
import com.tatum.service.data.BinaryManager;
import com.tatum.factory.BinaryManagerFactory;
import com.tatum.factory.imp.BinaryManagerFactoryImp;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ApplicationConfig {
    @Bean
    public PropertySource<?> yamlPropertySourceLoader() throws IOException {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        PropertySource<?> applicationYamlPropertySource = loader.load(
                "application.yml", new ClassPathResource("application.yml"),"default");
        return applicationYamlPropertySource;
    }

    @Bean(name="dataSource")
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/thedemo?useUnicode=true&amp;characterEncoding=UTF-8");
        dataSource.setUsername("demo");
        dataSource.setPassword("demo123");
        return dataSource;
    }

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Bean
    @Autowired
    public BinaryManager binaryManager(ApplicationProperties applicationProperties) {
        BinaryManagerConfig binaryManagerConfig = new BinaryManagerConfig();
        binaryManagerConfig.setEndpoint(applicationProperties.getRootLocalBinaryPath());
        BinaryManagerFactory binaryManagerFactory = new BinaryManagerFactoryImp(binaryManagerConfig);
        try {
            return binaryManagerFactory.createBinaryManager(Class.forName(applicationProperties.getBinaryManagerClass()));
        } catch (ClassNotFoundException e) {
            throw new DemoRuntimeException("Cannot found manager class",e);
        }
    }

    @Bean
    public GoogleProperties googleProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/google.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        GoogleProperties googleProperties = new GoogleProperties();
        googleProperties.setProperties(propertiesFactoryBean.getObject());
        return googleProperties;
    }
}
