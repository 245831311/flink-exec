package operator.join;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichCoGroupFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;


public class BroadcastOperator {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<String> nameDs = env.fromElements("man","jerry","aka","john","apple","cc");
		DataStream<String> infoDs = env.fromElements("apple","vv");
		
		DataStream<String> brocastDs = nameDs.broadcast().map(new MapFunction<String,String>(){
			@Override
			public String map(String value) throws Exception {
				System.out.println("---"+value+"---");
				return value;
			}
		}).setParallelism(2);
		
		brocastDs.print().setParallelism(3);
		
		env.execute();
	}
	private static class TimeStampExtractor implements TimestampAssigner<String> {
        @Override
        public long extractTimestamp(String element, long recordTimestamp) {
            return System.currentTimeMillis();
        }
    }
	
	private static class MyWatermarkGenerator implements WatermarkGenerator<String>, Serializable {

	    private final long maxTimeLag = 5000; // 5 �?

        @Override
        public void onEvent(
                String event, long eventTimestamp, WatermarkOutput output) {
            output.emitWatermark(new Watermark(System.currentTimeMillis()));
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            output.emitWatermark(new Watermark(System.currentTimeMillis()));
        }
    }
}
