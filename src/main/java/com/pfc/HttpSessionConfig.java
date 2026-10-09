/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pfc;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.mongo.JdkMongoSessionConverter;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

/**
 *
 * @author nocta
 */

@Configuration
@EnableMongoHttpSession
public class HttpSessionConfig {
    
    @Bean
    public JdkMongoSessionConverter jdkMongoSessionConverter() {
    return new JdkMongoSessionConverter(Duration.ofMinutes(30));
    }
}
