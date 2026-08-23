package ar.com.solaresdedonato.api.adapter.in.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PostgresTransactionManagerConfig {

    @Bean(name = {"transactionManager", "postgresTransactionManager"})
    @Primary
    public PlatformTransactionManager postgresTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
