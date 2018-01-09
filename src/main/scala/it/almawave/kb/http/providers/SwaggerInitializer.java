//package it.almawave.kb.http.providers;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//
//import io.swagger.jaxrs.config.BeanConfig;
//
//@WebListener
//public class SwaggerInitializer implements ServletContextListener {
//
//	public void contextInitialized(ServletContextEvent servletContextEvent) {
//		BeanConfig beanConfig = new BeanConfig();
//		beanConfig.setVersion("1.0.2");
//		// beanConfig.setResourcePackage("com.company.swagger.domain.rest");
//		beanConfig.setBasePath("http://localhost:7777/kb/api/v1/");
//		beanConfig.setDescription("katalod API");
//		beanConfig.setTitle("katalod API");
//		beanConfig.setScan(true);
//	}
//
//	public void contextDestroyed(ServletContextEvent servletContextEvent) {
//	}
//
//}