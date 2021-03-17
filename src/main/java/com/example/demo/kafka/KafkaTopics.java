package com.example.demo.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "kafka.topology.demo.topics")
@ConditionalOnProperty("kafka.bootstrap-servers")
public class KafkaTopics {
	@Value("${kafka.topology.demo.global-partitions}")
	private int globalPartitions;
	@Value("${kafka.topology.demo.global-replication}")
	private short globalReplication;
	@Value("${kafka.topology.demo.global-retention-ms}")
	private long globalRetentionMs;
	@Value("${kafka.topology.demo.global-delete-retention-ms}")
	private long globalDeleteRetentionMs;

	private TopicConfiguration events;

	@Bean
	public NewTopic demoTopic() {
		return new NewTopic(events.getName(), getPartitions(events), getReplication(events))
				.configs(events.getConfigs(globalRetentionMs, globalDeleteRetentionMs));
	}

	public TopicConfiguration getEvents() {
		return events;
	}

	public void setEvents(TopicConfiguration events) {
		this.events = events;
	}

	public static class TopicConfiguration {
		private String name;
		private int partitions;
		private short replication;
		private String cleanupPolicy;
		private long retentionMs;
		private long deleteRetentionMs;

		public Map<String, String> getConfigs(long globalRetentionMs, long globalDeleteRetentionMs) {
			Map<String, String> config = new HashMap<>();
			config.put(TopicConfig.CLEANUP_POLICY_CONFIG, cleanupPolicy);
			config.put(TopicConfig.RETENTION_MS_CONFIG, "compact".equals(cleanupPolicy) ? "-1"
					: retentionMs == 0 ? String.valueOf(globalRetentionMs) : String.valueOf(retentionMs));
			config.put(TopicConfig.DELETE_RETENTION_MS_CONFIG,
					deleteRetentionMs == 0 ? String.valueOf(globalDeleteRetentionMs)
							: String.valueOf(deleteRetentionMs));
			config.put(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG,
					deleteRetentionMs == 0 ? String.valueOf(globalDeleteRetentionMs)
							: String.valueOf(deleteRetentionMs));
			return config;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getPartitions() {
			return partitions;
		}

		public void setPartitions(int partitions) {
			this.partitions = partitions;
		}

		public short getReplication() {
			return replication;
		}

		public void setReplication(short replication) {
			this.replication = replication;
		}

		public String getCleanupPolicy() {
			return cleanupPolicy;
		}

		public void setCleanupPolicy(String cleanupPolicy) {
			this.cleanupPolicy = cleanupPolicy;
		}

		public long getRetentionMs() {
			return retentionMs;
		}

		public void setRetentionMs(long retentionMs) {
			this.retentionMs = retentionMs;
		}

		public long getDeleteRetentionMs() {
			return deleteRetentionMs;
		}

		public void setDeleteRetentionMs(long deleteRetentionMs) {
			this.deleteRetentionMs = deleteRetentionMs;
		}
	}

	private int getPartitions(TopicConfiguration configuration) {
		if (configuration.partitions == 0) {
			return globalPartitions;
		}
		return configuration.partitions;
	}

	private short getReplication(TopicConfiguration configuration) {
		if (configuration.replication == 0) {
			return globalReplication;
		}
		return configuration.replication;
	}
}