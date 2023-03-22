package com.example.springweb.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource productValidationProperties() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:validation/product");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource productValidationProperties) {
        var validatorFactory = new LocalValidatorFactoryBean();
        validatorFactory.setValidationMessageSource(productValidationProperties);
        return validatorFactory;
    }
}
