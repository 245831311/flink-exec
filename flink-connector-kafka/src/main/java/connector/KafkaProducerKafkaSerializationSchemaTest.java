package connector;

import java.util.Properties;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;

import entity.PageEvent;
import utils.DataGenerator;

public class KafkaProducerKafkaSerializationSchemaTest {

	
	private static final String topic = "string-schema-test";
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<PageEvent> pageDs = env.addSource(new DataGenerator());
		
		FlinkKafkaProducer producer = new FlinkKafkaProducer<>(topic,new PageEventProducerKakfaSchema2(),config(),FlinkKafkaProducer.Semantic.NONE);
		pageDs.addSink(producer);
		
		env.execute("print producer kafkaSerialize kafka");
	}
	
	
	private static Properties config(){
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "kafka:9092");
		return prop;
	}
}
