package org.example;

import java.util.List;

import org.aspect.PersistentAspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WsConfig extends WsConfigurerAdapter {

	@Autowired
	private CustomValidatingInterceptor interceptor;

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        final MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/v1/*");
    }

    @Bean
    public XsdSchema schema() {
        return new SimpleXsdSchema(new ClassPathResource("country.xsd"));
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        String[] jaxbContext = new String[] { "io.spring.guides.gs_producing_web_service" };
        marshaller.setContextPaths(jaxbContext);
        return marshaller;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        // aop not working
		//interceptors.add(new CustomValidatingInterceptor(schema(), config()));
        // aop working
        // interceptors.add(new CustomValidatingInterceptor(schema(), null));

		// Allowing Spring to autowire
		interceptors.add(interceptor);
    }

    @Bean
    public AppConfig config() {
        return new AppConfig();
    }

    @Bean
    public PersistentAspect persistentAspect() {
        PersistentAspect persistentAspect = new PersistentAspect();
        return persistentAspect;
    }

    @Bean
    public Object testAop(AppConfig config) {
        System.out.println("is config aop proxy: " + AopUtils.isAopProxy(config));

        return null;
    }
}
