package com.farmconnect.krishisetu.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;

@Configuration
@EnableConfigurationProperties(SupabaseSSLProperties.class)
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(
            SupabaseSSLProperties sslProps,
            org.springframework.core.env.Environment env
    ) throws Exception {

        String url = env.getProperty("spring.datasource.url");
        String user = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");

        File certFile = createCertFile(sslProps.getCert());

        HikariDataSource ds = new HikariDataSource();

        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);

        ds.addDataSourceProperty("sslmode", "verify-full");
        ds.addDataSourceProperty("sslrootcert", certFile.getAbsolutePath());

        return ds;
    }

    private File createCertFile(String cert) throws Exception {

        File file = File.createTempFile("supabase", ".crt");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cert.replace("\\n", "\n"));
        }

        file.deleteOnExit();

        return file;
    }
}