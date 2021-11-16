package connector;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;

import entity.PageEvent;

/**
 * 
 * @author Lenovo
 *
 */
public class PageEventProducerKakfaSchema2 implements KafkaSerializationSchema<PageEvent>{

	private static final Gson gson = new Gson();

	@Override
	public ProducerRecord<byte[], byte[]> serialize(PageEvent element, Long timestamp) {
		ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<byte[], byte[]>("string-schema-test","pageEvent".getBytes(),gson.toJson(element).getBytes());
		return producerRecord;
	}


}

