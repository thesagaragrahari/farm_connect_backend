package com.farmconnect.krishisetu.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class SupabaseSSLInitializer implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment env,
            SpringApplication application
    ) {

        try {

            String url = env.getProperty("spring.datasource.url");
            String cert = env.getProperty("supabase.ssl.cert");

            if (url == null) return;

            if (cert != null && !cert.isEmpty()) {

                Path tempFile = Files.createTempFile("supabase", ".crt");

                Files.write(
                        tempFile,
                        cert.replace("\\n", "\n")
                                .getBytes(StandardCharsets.UTF_8)
                );

                tempFile.toFile().deleteOnExit();

                url =
                        url
                        + "?sslmode=verify-full"
                        + "&sslrootcert="
                        + tempFile.toAbsolutePath();

            }

            System.setProperty(
                    "spring.datasource.url",
                    url
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}