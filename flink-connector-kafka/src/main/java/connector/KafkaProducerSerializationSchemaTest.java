package connector;

import java.util.Properties;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import entity.PageEvent;
import utils.DataGenerator;

public class KafkaProducerSerializationSchemaTest {

	
	private static final String topic = "string-schema-test";
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "kafka:9092");
		
		DataStream<PageEvent> pageDs = env.addSource(new DataGenerator());
		
		FlinkKafkaProducer<PageEvent> producer = new FlinkKafkaProducer<PageEvent>(topic, new PageEventProducerKakfaSchema(), prop);

		pageDs.addSink(producer).name("kafka producer sink");
		
		env.execute("print producer string kafka");
	}
}
