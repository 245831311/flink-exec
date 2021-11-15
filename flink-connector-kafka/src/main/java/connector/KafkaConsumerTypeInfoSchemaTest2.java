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
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.streaming.util.serialization.TypeInformationKeyValueSerializationSchema;
import org.apache.flink.util.Collector;

import entity.PageEvent;

public class KafkaConsumerTypeInfoSchemaTest2 {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "1ae376193e8a:9092");
		prop.setProperty("group.id", "test");
		
		//json schema
		TypeInformation<PageEvent> typeInfo = TypeInformation.of(PageEvent.class);
		TypeInformationSerializationSchema<PageEvent> schema = new TypeInformationSerializationSchema(typeInfo, env.getConfig());
		DataStream<PageEvent> ds =  env.addSource(new FlinkKafkaConsumer<>("input",schema, prop), "kafka-test");
		DataStream<String> ds2 = ds.map(x->x.getPage());
		ds2.print();
		
		env.execute("print kafka");
	}
}
