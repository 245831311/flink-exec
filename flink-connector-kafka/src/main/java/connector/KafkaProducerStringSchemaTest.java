package connector;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.streaming.util.serialization.TypeInformationKeyValueSerializationSchema;
import org.apache.flink.util.Collector;

public class KafkaProducerStringSchemaTest {

	private static final String topic = "string-schema-test";
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "1ae376193e8a:9092");
		

		FlinkKafkaProducer<String> sink = new FlinkKafkaProducer<>(topic, new SimpleStringSchema(), prop);
		
		env.execute("print kafka");
	}
}
