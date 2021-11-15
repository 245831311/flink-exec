package connector;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.google.gson.Gson;

import entity.PageEvent;

/**
 * 
 * @author Lenovo
 *
 */
public class PageEventKakfaSchema implements KafkaDeserializationSchema<PageEvent> {

	@Override
	public TypeInformation<PageEvent> getProducedType() {
		// TODO Auto-generated method stub
		return TypeInformation.of(PageEvent.class);
	}

	@Override
	public boolean isEndOfStream(PageEvent nextElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PageEvent deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
		if(record.key() != null){
			String key = new String(record.key());
		}
		String value = new String(record.value());
		return new PageEvent();
	}
}

