package tatum.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import tatum.app.exception.DemoRuntimeException;
import tatum.app.factory.config.BinaryManagerConfig;
import tatum.app.manager.data.BinaryManager;
import tatum.app.factory.BinaryManagerFactory;
import tatum.app.factory.imp.BinaryManagerFactoryImp;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

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

    @Bean
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
}
