package operator.window;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

public class KeyByWindowOperator {

	
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "1ae376193e8a:9092");
		prop.setProperty("group.id", "test");
		FlinkKafkaConsumer<PageEvent> consumer = new FlinkKafkaConsumer<PageEvent>("string-schema-test",new PageEventSchema(), prop);
		DataStream<PageEvent> peDs =  env.addSource(consumer, "kafka-test");
		
		peDs.keyBy("userId")
		.window(TumblingTimeWindows.of(Time.minutes(1)))
		.process(new MyProcessFunction());
		peDs.print();
		env.execute();
	}
	
	
	public static class MyProcessFunction extends ProcessWindowFunction
		<PageEvent,Tuple4<Long,Long,String,Integer>,Tuple,TimeWindow>{

		@Override
		public void process(Tuple tuple,Context context,
				Iterable<PageEvent> in, Collector<Tuple4<Long, Long, String, Integer>> out) throws Exception {
			Iterator<PageEvent> pageIt = in.iterator();
			int count = 0;
			while(pageIt.hasNext()){
				pageIt.next();
				++count;
			}
            TimeWindow window = context.window();
			out.collect(Tuple4.of(window.getStart(), window.getEnd(), null, count));
		}


	}
	
}
