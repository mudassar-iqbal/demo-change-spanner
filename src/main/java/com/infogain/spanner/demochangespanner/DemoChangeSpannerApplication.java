package com.infogain.spanner.demochangespanner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.publisher.SpannerDatabaseChangeEventPublisher;
import com.google.cloud.spanner.publisher.SpannerToJsonFactory;
import com.google.cloud.spanner.watcher.SpannerDatabaseChangeWatcher;
import com.google.cloud.spanner.watcher.SpannerDatabaseTailer;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
@PropertySource("classpath:scep.properties")
public class DemoChangeSpannerApplication {

	  @Autowired
	  Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoChangeSpannerApplication.class, args);
	}
	
//	@Bean
//	public Properties readProperties() {
//		Properties props = new Properties();
//		MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
//		StreamSupport.stream(propSrcs.spliterator(), false)
//		        .filter(ps -> ps instanceof EnumerablePropertySource)
//		        .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
//		        .flatMap(Arrays::<String>stream)
//		        .forEach(propName -> props.setProperty(propName, env.getProperty(propName)));
//		log.info("start reading property");
//		log.info(props.getProperty("scep.maxWaitForShutdownSeconds"));
//		log.info(env.getProperty("scep.maxWaitForShutdownSeconds"));
//		log.info("end reading property");
//		return props;
//	}
	
//	@Bean
//	public SpannerDatabaseChangeWatcher createSpannerDatabaseChangeWatcher() {
//		String instance = env.getProperty("scep.spanner.instance");
//	    String database = env.getProperty("scep.spanner.database");
//	    String topic = "spannertopic";
//	    String subscription = "spannertopic-sub";
//	    SpannerOptions options = SpannerOptions.newBuilder().build();
//	    String project = options.getProjectId();
//	    // Create a connection to a Spanner database.
//	    log.info(
//	        String.format(
//	            "Connecting to projects/%s/instances/%s/databases/%s...",
//	            options.getProjectId(), instance, database));
//	    Spanner spanner = options.getService();
//	    DatabaseId databaseId = DatabaseId.of(project, instance, database);
//		SpannerDatabaseChangeWatcher watcher =
//		        SpannerDatabaseTailer.newBuilder(spanner, databaseId).allTables().build();
//		
//		return watcher;
//	}

	@Bean
	public SpannerDatabaseChangeEventPublisher createSpannerDatabaseChangeEventPublisher() throws IOException {
		String instance = env.getProperty("scep.spanner.instance");
	    String database = env.getProperty("scep.spanner.database");
	    String topic = "spannertopic";
	    String subscription = "spannertopic-sub";
	    SpannerOptions options = SpannerOptions.newBuilder().build();
	    String project = options.getProjectId();
	    // Create a connection to a Spanner database.
	    log.info(
	        String.format(
	            "Connecting to projects/%s/instances/%s/databases/%s...",
	            options.getProjectId(), instance, database));
	    Spanner spanner = options.getService();
	    DatabaseId databaseId = DatabaseId.of(project, instance, database);
		SpannerDatabaseChangeWatcher watcher =
		        SpannerDatabaseTailer.newBuilder(spanner, databaseId).allTables().build();
		// Then create a change publisher using the change watcher.
	    DatabaseClient client = spanner.getDatabaseClient(databaseId);
	    SpannerDatabaseChangeEventPublisher publisher =
	        SpannerDatabaseChangeEventPublisher.newBuilder(watcher, client)
	            .setConverterFactory(SpannerToJsonFactory.INSTANCE)
	            .setTopicNameFormat(String.format("projects/%s/topics/%s", project, topic))
	            .setCreateTopicsIfNotExist(true)
	            .build();
	    
	    return publisher;
	}
	
	

}
