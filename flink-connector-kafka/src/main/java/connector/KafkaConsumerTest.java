package connector;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaConsumerTest {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "bf4526ae869c:9092");
		prop.setProperty("group.id", "test");
		
		DataStream<String> ds =  env.addSource(new FlinkKafkaConsumer<>("input",new SimpleStringSchema(), prop), "kafka-test");
	
		ds.print();
		
		env.execute("print kafka");
	}
}
