package com.infogain.spanner.demochangespanner.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.AcknowledgeRequest;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PullRequest;
import com.google.pubsub.v1.PullResponse;
import com.google.pubsub.v1.ReceivedMessage;
import com.infogain.spanner.demochangespanner.entity.Singer;
import com.infogain.spanner.demochangespanner.repository.SingerRepository;
import com.infogain.spanner.demochangespanner.service.PublisherService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class InsertController {

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	private SingerRepository singerRepo;
	@Autowired
	PublisherService publisherService;
	
	@PostMapping("/insertRecords")
	public String publishMessage() {
		
		return "Ok";
	}
	
	@GetMapping("/getRecords")
	public ResponseEntity<List<Singer>> getSingers() {
		//Singer singer1 = new Singer(20,"Test","Test");
		//this.singerRepo.save(singer1);
		List<Singer> listSinger = new ArrayList<>();
		 this.singerRepo.findAll().forEach(new Consumer<Singer>() { 
  
            @Override
            public void accept(Singer t) 
            { 
            	listSinger.add(t);
                //System.out.println(t); 
            } 
  
        });
		//log.info(listSinger.);
		 
		 //ObjectNode objectNode = mapper.createObjectNode();
		 //objectNode.
		return new ResponseEntity<List<Singer>>(listSinger, HttpStatus.OK);
	}
	
	@GetMapping("/getMessages")
	public List<ObjectNode> getMessage() {
		String projectId = "iconic-scholar-307206";
	    String subscriptionId = "spannertopic-sub";
	    Integer numOfMessages = 20;
	    List<ObjectNode> listObjectNode = new ArrayList<>();
	    try {
	    	log.info("pulling messages 1");
	    	listObjectNode = subscribeSyncExample(projectId, subscriptionId, numOfMessages);
	    	log.info("pulling messages 11");
	    } catch (IOException e) {
			log.error("error pulling mesages", e);
			//e.printStackTrace();
		}
		return listObjectNode;
		
	}
	
	
	public static List<ObjectNode> subscribeSyncExample(
		      String projectId, String subscriptionId, Integer numOfMessages) throws IOException {
		//log.info("pulling messages 2");    
		SubscriberStubSettings subscriberStubSettings =
		        SubscriberStubSettings.newBuilder()
		            .setTransportChannelProvider(
		                SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
		                    .setMaxInboundMessageSize(20 * 1024 * 1024) // 20MB (maximum message size).
		                    .build())
		            .build();
		//log.info("pulling messages 3");
		    List<ObjectNode> listObjectNode = new ArrayList<>();
		    try (SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
		    	//log.info("pulling messages 4");
		      String subscriptionName = ProjectSubscriptionName.format(projectId, subscriptionId);
		      //log.info("pulling messages 5");
		      PullRequest pullRequest =
		          PullRequest.newBuilder()
		              .setMaxMessages(numOfMessages)
		              .setSubscription(subscriptionName)
		              .build();
		      //log.info("pulling messages 6");
		      // Use pullCallable().futureCall to asynchronously perform this operation.
		      PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);
		      //log.info("pulling messages 7");
		      List<String> ackIds = new ArrayList<>();
		      
		      for (ReceivedMessage message : pullResponse.getReceivedMessagesList()) {
		        // Handle received message
		    	//message.
		    	log.info("--logging message--");
		    	log.info(message.getMessage().getData().toStringUtf8());
		    	ObjectNode json = new ObjectMapper().readValue(message.getMessage().getData().toStringUtf8(), ObjectNode.class);
		    	listObjectNode.add(json);
		        // ...
		        ackIds.add(message.getAckId());
		      }
		      //log.info("pulling messages 8");
		      // Acknowledge received messages.
		      
		      if(!pullResponse.getReceivedMessagesList().isEmpty()) {
		      AcknowledgeRequest acknowledgeRequest =
		          AcknowledgeRequest.newBuilder()
		              .setSubscription(subscriptionName)
		              .addAllAckIds(ackIds)
		              .build();
		      log.info("pulling messages 9");
		      // Use acknowledgeCallable().futureCall to asynchronously perform this operation.
		      subscriber.acknowledgeCallable().call(acknowledgeRequest);
		      log.info("pulling messages 10");
		    }
		      //System.out.println(pullResponse.getReceivedMessagesList());
		    }
		    return listObjectNode;
		  }
	
	@GetMapping("/hello")
	public ObjectNode sayHello() {
	    ObjectNode objectNode = mapper.createObjectNode();
	    objectNode.put("key", "value");
	    objectNode.put("foo", "bar");
	    objectNode.put("number", 42);
	    return objectNode;
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
	    log.info("spanner publisher started up");
	    publisherService.execute();
	}
	
	
}
