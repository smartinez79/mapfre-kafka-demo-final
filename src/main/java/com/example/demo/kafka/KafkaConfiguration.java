package com.example.demo.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.demo.model.events.DemoEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnProperty("kafka.bootstrap-servers")
public class KafkaConfiguration {

	private final String bootstrapServers;
	private final String apiKey;
	private final String apiSecret;
	private final String clientId;
	private final String consumerId;
	private final int retries;
	private final ObjectMapper objectMapper;

	public KafkaConfiguration(@Value("${spring.application.name}") String appName,
			@Value("${kafka.bootstrap-servers}") String bootstrapServers,
			@Value("${kafka.api-key:#{null}}") String apiKey,
			@Value("${kafka.api-secret:#{null}}") String apiSecret,
			@Autowired ObjectMapper objectMapper,
			@Value("${kafka.producer.retries}") int retries) {

		this.clientId = appName;
		this.consumerId = appName;
		this.bootstrapServers = bootstrapServers;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.objectMapper = objectMapper;
		this.retries = retries;
	}

	@Bean
	public ProducerFactory<Object, Object> kafkaProducerFactory() {
		return getFactory(producerConfigs());
	}

	private <T> ProducerFactory<T, Object> getFactory(Map<String, Object> configs) {
		DefaultKafkaProducerFactory<T, Object> producerFactory = new DefaultKafkaProducerFactory<>(configs);

		JsonSerializer<Object> serializer = new JsonSerializer<>(objectMapper);
		serializer.setAddTypeInfo(false);
		producerFactory.setValueSerializer(serializer);
		producerFactory.setTransactionIdPrefix("demo-kafka." + UUID.randomUUID());
		
		return producerFactory;
	}

	private Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		addAuthConfigProperties(props);
		props.put(ProducerConfig.RETRIES_CONFIG, retries);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		return props;
	}


	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, DemoEvent> demoEventListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, DemoEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(demoConsumerFactory());
		return factory;
	}
	
	
	private ConsumerFactory<String, DemoEvent> demoConsumerFactory() {
		Map<String,Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
		addAuthConfigProperties(props);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, this.consumerId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, DemoEvent.class);

		JsonDeserializer<DemoEvent> valueDeserializer = new JsonDeserializer<>(DemoEvent.class, objectMapper);
		valueDeserializer.setUseTypeHeaders(false);

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDeserializer);
	}


	@Bean
	public KafkaAdmin admin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		addAuthConfigProperties(configs);
		return new KafkaAdmin(configs);
	}

	private void addAuthConfigProperties(Map<String, Object> props) {
		if (apiKey != null) {
			props.put("ssl.endpoint.identification.algorithm", "https");
			props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
			props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
			props.put(SaslConfigs.SASL_JAAS_CONFIG,
					"org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" +
							apiKey + "\" password=\"" + apiSecret + "\";");
		}
	}


}