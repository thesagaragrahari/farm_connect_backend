package com.farmconnect.krishisetu.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;


@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${supabase.ssl.certification:}") // fallback empty if not set
    private String sslCertContent;

    @Bean
    public DataSource dataSource() throws IOException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(dbUsername);
        dataSource.setPassword(dbPassword);

        String finalUrl = dbUrl;

        if (!sslCertContent.isEmpty()) {
            // Write cert content to a temp file
            Path tempFile = Files.createTempFile("supabase-ssl", ".crt");
            // Replace literal "\n" with actual newline
            Files.write(tempFile, sslCertContent.replace("\\n", "\n").getBytes(StandardCharsets.UTF_8));
            tempFile.toFile().deleteOnExit();

            finalUrl += "?sslmode=verify-full&sslrootcert=" + tempFile.toAbsolutePath();
        } else {
            finalUrl += "?sslmode=require"; // fallback if no cert
        }

        dataSource.setURL(finalUrl);
        return dataSource;
    }
}
