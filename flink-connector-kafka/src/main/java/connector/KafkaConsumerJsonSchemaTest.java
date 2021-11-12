package connector;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.util.Collector;

public class KafkaConsumerJsonSchemaTest {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "1ae376193e8a:9092");
		prop.setProperty("group.id", "test");
		
		//json schema
		DataStream<ObjectNode> ds =  env.addSource(new FlinkKafkaConsumer<>("input",new JSONKeyValueDeserializationSchema(true), prop), "kafka-test");
		DataStream<String> ds2 = ds.map(x->{
			return x.get("value").get("page").asText();
		});
		/*DataStream<String> ds2 = ds.map(x->x.get("value").get("page").asText())
				.flatMap(new FlatMapFunction<String,String>() {
					@Override

					public void flatMap(String line, Collector out) throws Exception {
						out.collect(line);
					}

		});*/
		
		
		ds2.print();
		
		env.execute("print kafka");
	}
}
