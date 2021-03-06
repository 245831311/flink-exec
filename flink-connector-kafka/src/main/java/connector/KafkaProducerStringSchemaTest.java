package connector;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import com.google.gson.Gson;

import entity.PageEvent;
import utils.DataGenerator;

public class KafkaProducerStringSchemaTest {

	private static final Gson gson = new Gson();

	
	private static final String topic = "string-schema-test";
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "1ae376193e8a:9092");
		
		DataStream<PageEvent> pageDs = env.addSource(new DataGenerator());
		
		DataStream<String> strDs = pageDs.map(x->gson.toJson(x));
		
		FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(topic, new SimpleStringSchema(), prop);
		strDs.addSink(producer).name("kafka producer sink");
		
		env.execute("print producer string kafka");
	}
}
