/*
 * Copyright (C) 2020 Consiglio Nazionale delle Ricerche
 *
 *                 This program is free software: you can redistribute it and/or modify
 *                 it under the terms of the GNU Affero General Public License as
 *                 published by the Free Software Foundation, either version 3 of the
 *                 License, or (at your option) any later version.
 *
 *                 This program is distributed in the hope that it will be useful,
 *                 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *                 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *                 GNU Affero General Public License for more details.
 *
 *                 You should have received a copy of the GNU Affero General Public License
 *                 along with this program. If not, see https://www.gnu.org/licenses/
 */

package it.cnr.si.config;

import feign.Feign;
import feign.Request;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import it.cnr.si.repository.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty({"print.url"})
@EnableConfigurationProperties(PrintConfigurationProperties.class)
public class PrintConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintConfigurationProperties.class);

    private static final Integer CONNECT_TIMEOUT = 1000;
    private static final Integer READ_TIMEOUT = 6000;
    private static final Boolean FOLLOW_REDIRECT = false;

    @Bean
    public Print print(PrintConfigurationProperties printConfigurationProperties) {

        String url = printConfigurationProperties.getUrl();
        LOGGER.info("Connecting to print server ... {}", url);

        okhttp3.OkHttpClient clientWithInterceptor = new okhttp3.OkHttpClient()
            .newBuilder()
            .build();

        return Feign.builder()
            .client(new OkHttpClient(clientWithInterceptor))
            .decoder(new GsonDecoder())
            .encoder(new FormEncoder(new GsonEncoder()))
            .options(new Request.Options(CONNECT_TIMEOUT, READ_TIMEOUT, FOLLOW_REDIRECT))
            .target(Print.class, url);
    }

}
