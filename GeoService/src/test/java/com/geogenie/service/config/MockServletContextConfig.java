package com.geogenie.service.config;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.geogenie.geo.service.config.AppSecurityConfig;
import com.geogenie.geo.service.config.HibernateConfiguration;
import com.geogenie.geo.service.config.ServletContextConfig;
import com.geogenie.geo.service.config.SpringConfiguration;

/**
 * Mock Class for Servlet Context.
 * 
 * @author ajoinw
 * 
 */
@Configuration
@EnableWebMvc
public class MockServletContextConfig extends WebMvcConfigurerAdapter {

	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/");
	}
	
	
}
