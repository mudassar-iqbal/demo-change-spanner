package com.infogain.spanner.demochangespanner.service;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.cloud.spanner.publisher.SpannerDatabaseChangeEventPublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PublisherService {
	@Autowired
	Environment env;
	
	@Autowired
	SpannerDatabaseChangeEventPublisher publisher;
	
	public void execute() {
		log.info("Start the change publisher and wait until it is running.");
		// Start the change publisher and wait until it is running.
	    publisher.startAsync().awaitRunning();
	    log.info("Successfully started.");
	}
	
	@PreDestroy
	public void stop() {
		log.info("Closing publisher.");
		// Start the change publisher and wait until it is running.
		publisher.stopAsync();
		publisher.awaitTerminated();
		log.info("Closed publisher.");
	}
}
