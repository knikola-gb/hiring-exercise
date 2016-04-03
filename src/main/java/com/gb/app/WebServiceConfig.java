package com.gb.app;

import com.gb.app.soap.SoapFaultDefinitionExceptionResolver;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 03.04.2016
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/soap/*");
    }

    @Bean(name = "vat")
    public DefaultWsdl11Definition defaultWsdl11Definition1(XsdSchema vatSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("VatPort");
        wsdl11Definition.setLocationUri("/soap/vat");
        wsdl11Definition.setTargetNamespace("http://gb.com/hiring-exercise");
        wsdl11Definition.setSchema(vatSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema vatSchema() {
        return new SimpleXsdSchema(new ClassPathResource("vat.xsd"));
    }

    @Bean(name = "soapFaultAnnotationExceptionResolver")
    public SoapFaultDefinitionExceptionResolver exceptionResolver(ApplicationContext applicationContext) {
        SoapFaultDefinitionExceptionResolver exceptionResolver = new SoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
        soapFaultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(soapFaultDefinition);

        return exceptionResolver;
    }
}
