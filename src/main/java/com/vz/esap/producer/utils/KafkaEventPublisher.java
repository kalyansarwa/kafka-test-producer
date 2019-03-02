package com.vz.esap.producer.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher {

	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;

	@Value("${kafka.acks}")
	private String acksConfiguration;

	@Value("${kafka.retries}")
	private Integer retries;

	@Value("${kafka.linger.ms}")
	private Integer lingerConfiguration;

	@Value("${kafka.batch.size}")
	private Integer batchSize;

	@Value("${kafka.buffer.memory}")
	private Long bufferMemory;

	@Value("${kafka.max.in.flight.requests.per.connection}")
	private Integer maxInFlightRequests;

	@Value("${kafka.enable.auto.commit}")
	private boolean autoCommit;

	@Value("${kafka.topic}")
	private String kafkaSiteTopic;

	@Value("${kafka.enableNotification}")
	private boolean enableKafkaNotifications;

	private Producer<String, String> kafkaProducer;

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventPublisher.class);

	@PostConstruct
	public void initKakfaProducer() {
		if (enableKafkaNotifications) {
			Map<String, Object> producerConfiguration = new HashMap<String, Object>();
			producerConfiguration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
			producerConfiguration.put(ProducerConfig.RETRIES_CONFIG, retries);
			producerConfiguration.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
			producerConfiguration.put(ProducerConfig.LINGER_MS_CONFIG, lingerConfiguration);
			producerConfiguration.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
			producerConfiguration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerConfiguration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			producerConfiguration.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequests);
			producerConfiguration.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "150000");
			kafkaProducer = new KafkaProducer<>(producerConfiguration);
		}
	}

	public void publishData(String eventData) {
		publishData(eventData, kafkaSiteTopic);
	}

	public void publishData(String eventData, String entityTopic) {
		if (!enableKafkaNotifications)
			return;
		LOGGER.info("PUBLISHING TO KAKFA-TOPIC: {}\n DATA:{}", entityTopic, eventData);
		ProducerRecord<String, String> dataToPublish = new ProducerRecord<String, String>(entityTopic, eventData);
		try {
			kafkaProducer.send(dataToPublish, new Callback() {
				public void onCompletion(RecordMetadata metadata, Exception e) {
					if (e != null) {
						LOGGER.error("EXCEPTION OCCURRED WHILE PUBLISHING DATA TO TOPIC: {}", entityTopic);
						e.printStackTrace();
					}
					if (metadata != null)
						LOGGER.info("The OFFSET OF RECORD PUBLISHED TO TOPIC : {} FOR ENTITY_TYPE: {} IS : ",
								entityTopic, eventData, metadata.offset());
				}
			});
		} catch (Exception ex) {
			LOGGER.error("ERROR PULBLISHING EVENT DATA TO KAFKA: {}", ex);
		}

	}

}
