package connector;

import java.util.Properties;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;

import entity.PageEvent;
import utils.DataGenerator;

public class KafkaProducerTypeInfoSchemaTest {

	
	private static final String topic = "string-schema-test";
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<PageEvent> pageDs = env.addSource(new DataGenerator());
		
		TypeInformation<PageEvent> typeInfo = Types.POJO(PageEvent.class);
		
		TypeInformationSerializationSchema<PageEvent> schema = new TypeInformationSerializationSchema<PageEvent>(typeInfo, env.getConfig());
		
		FlinkKafkaProducer<PageEvent> producer = new FlinkKafkaProducer<PageEvent>(topic,schema,config());
		pageDs.addSink(producer);
		
		env.execute("print producer kafkaSerialize kafka");
	}
	
	
	private static Properties config(){
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "kafka:9092");
		return prop;
	}
}
