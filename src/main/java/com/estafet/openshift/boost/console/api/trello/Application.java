package com.estafet.openshift.boost.console.api.trello;

import io.opentracing.Tracer;
import io.opentracing.contrib.jms.spring.TracingJmsTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.jms.ConnectionFactory;

@EnableJms
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public io.opentracing.Tracer jaegerTracer() {
        return new com.uber.jaeger.Configuration(System.getenv("JAEGER_SERVICE_NAME"),
                com.uber.jaeger.Configuration.SamplerConfiguration.fromEnv(),
                com.uber.jaeger.Configuration.ReporterConfiguration.fromEnv()).getTracer();
    }
    
    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}
    
    @Bean
	public MessageConverter tracingJmsMessageConverter(Tracer tracer) {
		return new PropagatingTracingMessageConverter(new SimpleMessageConverter(), tracer);
	}

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, Tracer tracer) {
        JmsTemplate jmsTemplate = new TracingJmsTemplate(connectionFactory, tracer);
        jmsTemplate.setMessageConverter(new SimpleMessageConverter());
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
