package connector;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;

import entity.PageEvent;

/**
 * 
 * @author Lenovo
 *
 */
public class PageEventProducerKakfaSchema implements SerializationSchema<PageEvent> {

	private static final Gson gson = new Gson();

	/*@Override
	public ProducerRecord<byte[], byte[]> serialize(PageEvent element, Long timestamp) {
		ProducerRecord<byte[], byte[]> producerRecord = new ProducerRecord<byte[], byte[]>("string-schema-test",gson.toJson(element).getBytes());
		return producerRecord;
	}*/

	@Override
	public byte[] serialize(PageEvent element) {
		// TODO Auto-generated method stub
		return gson.toJson(element).getBytes();
	}

}

