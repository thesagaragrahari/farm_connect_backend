package com.farmconnect.krishisetu.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    

    @Bean
    @ConfigurationProperties("supabase.ssl")
    public SupabaseSSLProperties sslProperties() {
        return new SupabaseSSLProperties();
    }

    @Bean
    public DataSource dataSource(
            DataSourceProperties props,
            SupabaseSSLProperties sslProps
    ) throws IOException {

        PGSimpleDataSource ds = new PGSimpleDataSource();

        ds.setUser(props.getUsername());
        ds.setPassword(props.getPassword());

        String finalUrl = props.getUrl();

        if (sslProps.getCertification() != null &&
                !sslProps.getCertification().isEmpty()) {

            Path tempFile = Files.createTempFile("supabase-ssl", ".crt");

            Files.write(
                    tempFile,
                    sslProps.getCertification()
                            .replace("\\n", "\n")
                            .getBytes(StandardCharsets.UTF_8)
            );

            tempFile.toFile().deleteOnExit();

            finalUrl += "?sslmode=verify-full&sslrootcert="
                    + tempFile.toAbsolutePath();

        } else {
            finalUrl += "?sslmode=require";
        }

        ds.setURL(finalUrl);

        return ds;
    }
}