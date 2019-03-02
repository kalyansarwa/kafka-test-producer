package com.vz.esap.producer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vz.esap.producer.service.MessageProducer;

@RestController
@RequestMapping("/kafkatest")
public class KafkaTestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTestController.class);
	private String message = "produced a message with number ";
	private int count = 1;
	
	@Autowired
	MessageProducer msgProducer;

	@RequestMapping(value = "/produce", method = RequestMethod.GET, produces = "application/json")
	public void ProduceMessage() throws Exception {
		LOGGER.info("========================");
		msgProducer.producer("ESVRRS Kafka Test Producer " + message + (count++));
	}
}
