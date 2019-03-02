package com.vz.esap.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vz.esap.producer.utils.KafkaEventPublisher;

@Service
public class MessageProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);
	
	@Autowired
	private KafkaEventPublisher kafkaEventPublisher;
	
	public void producer(String msg) throws Exception {
		
		LOGGER.info("Entering kafkaEvent to publish message data: ", msg);
		kafkaEventPublisher.publishData(msg);
	}

}
