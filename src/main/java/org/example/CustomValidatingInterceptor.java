package org.example;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.support.interceptor.WebServiceValidationException;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.xml.xsd.XsdSchema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@Component
public class CustomValidatingInterceptor extends PayloadValidatingInterceptor {

	@Autowired
	private XsdSchema schema;

	@Autowired
	private ObjectFactory<AppConfig> konfigurace;

	@Override
	public void afterPropertiesSet() throws Exception {
		setXsdSchema(schema);
		super.afterPropertiesSet();
	}

	@Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint)
            throws IOException, SAXException, TransformerException {
		System.out.println("is config aop proxy in interceptor: " +
				AopUtils.isAopProxy(konfigurace.getObject()));
        return super.handleRequest(messageContext, endpoint);
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws IOException, SAXException {
        return super.handleResponse(messageContext, endpoint);
    }

    @Override
    protected boolean handleRequestValidationErrors(MessageContext messageContext, SAXParseException[] errors)
            throws TransformerException {
        return super.handleRequestValidationErrors(messageContext, errors);
    }

    @Override
    protected boolean handleResponseValidationErrors(MessageContext messageContext, SAXParseException[] errors)
            throws WebServiceValidationException {
        return super.handleResponseValidationErrors(messageContext, errors);
    }

    @Override
    protected Source getValidationRequestSource(WebServiceMessage request) {
        return super.getValidationRequestSource(request);
    }

    @Override
    protected Source getValidationResponseSource(WebServiceMessage response) {
        return super.getValidationResponseSource(response);
    }

}
