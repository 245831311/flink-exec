package operator.window;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;


public class KeyByWindowOperator {

	private static String outputPath = "F:/flink/jaydon";
	
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties prop = new Properties();
		prop.setProperty("bootstrap.servers", "1ae376193e8a:9092");
		prop.setProperty("group.id", "test");
		FlinkKafkaConsumer<PageEvent> consumer = new FlinkKafkaConsumer<PageEvent>("string-schema-test",new PageEventSchema(), prop);
		
		consumer.assignTimestampsAndWatermarks(
				WatermarkStrategy
				.<PageEvent>forBoundedOutOfOrderness(Duration.ofSeconds(20))
				.withTimestampAssigner((x,y)->Long.valueOf(x.timestamp))
				);
		DataStream<PageEvent> peDs =  env.addSource(consumer, "kafka-test");
		
		/*DataStream<Tuple5<String,String,String,String,Integer>> keyByPeDs = peDs.keyBy("userId")
		.window(TumblingEventTimeWindows.of(Time.milliseconds(1000)))
		.process(new MyProcessFunction()).print();*/
		
		/*DataStream<Tuple5<String,String,String,String,Integer>> keyByPeDs = peDs
				.keyBy("userId","page")
				.window(SlidingEventTimeWindows.of(Time.minutes(1), Time.milliseconds(10000)))
				.process(new MyProcessFunction2());*/
		
		DataStream<Tuple5<String,String,String,String,Integer>> keyByPeDs = peDs
				.keyBy("userId","page")
				.countWindow(10)
				.process(new MyCountProcessFunction());
		final StreamingFileSink<Tuple5<String,String,String,String,Integer>>  sf = StreamingFileSink.forRowFormat(new Path(outputPath), new SimpleStringEncoder<Tuple5<String,String,String,String,Integer>>("UTF-8"))
			    .withRollingPolicy(
			            DefaultRollingPolicy.builder()
			                .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
			                .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
			                .withMaxPartSize(1024 * 1024 * 1024)
			                .build())
			    	.build();
		keyByPeDs.addSink(sf);
		env.execute();
	}
	
	
	public static class MyProcessFunction extends ProcessWindowFunction
		<PageEvent,Tuple4<String,String,String,Integer>,Tuple,TimeWindow>{

		@Override
		public void process(Tuple tuple,Context context,
				Iterable<PageEvent> in, Collector<Tuple4<String, String, String, Integer>> out) throws Exception {
			Iterator<PageEvent> pageIt = in.iterator();
			int count = 0;
			while(pageIt.hasNext()){
				pageIt.next();
				++count;
			}
            TimeWindow window = context.window();
            
            Date start = new Date(window.getStart());
            Date end = new Date(window.getEnd());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			out.collect(Tuple4.of(sdf.format(start),sdf.format(end) , tuple.getField(0), count));
		}

	}
	
	public static class MyProcessFunction2 extends ProcessWindowFunction
	<PageEvent,Tuple5<String,String,String,String,Integer>,Tuple,TimeWindow>{

	@Override
	public void process(Tuple tuple,Context context,
			Iterable<PageEvent> in, Collector<Tuple5<String, String, String,String, Integer>> out) throws Exception {
		Iterator<PageEvent> pageIt = in.iterator();
		int count = 0;
		while(pageIt.hasNext()){
			pageIt.next();
			++count;
		}
        TimeWindow window = context.window();
        
        Date start = new Date(window.getStart());
        Date end = new Date(window.getEnd());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		out.collect(Tuple5.of(sdf.format(start),sdf.format(end) , tuple.getField(0),tuple.getField(1), count));
	}
	
}
	public static class MyCountProcessFunction extends ProcessWindowFunction
	<PageEvent,Tuple5<String,String,String,String,Integer>,Tuple,GlobalWindow>{

		@Override
		public void process(Tuple tuple,Context context,
				Iterable<PageEvent> in, Collector<Tuple5<String,String,String,String, Integer>> out) throws Exception {
			Iterator<PageEvent> pageIt = in.iterator();
			int count = 0;
			while(pageIt.hasNext()){
				pageIt.next();
				++count;
			}
	        GlobalWindow window = context.window();
	        Date start = new Date(window.maxTimestamp());
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			out.collect(Tuple5.of(sdf.format(start),null, tuple.getField(0),tuple.getField(1), count));
		}
	}
}
