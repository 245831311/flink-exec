package operator.join;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichCoGroupFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;


public class CoGroupOperator {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<String> nameDs = env.fromElements("man","jerry","aka","john","apple","cc")
				.assignTimestampsAndWatermarks(
						WatermarkStrategy
						.forGenerator((ctx) ->new MyWatermarkGenerator())
						.withTimestampAssigner((x)->new TimeStampExtractor())
						);
		DataStream<String> infoDs = env.fromElements("apple","vv")
				.assignTimestampsAndWatermarks(
						WatermarkStrategy
						.forGenerator((ctx) ->new MyWatermarkGenerator())
						.withTimestampAssigner((x)->new TimeStampExtractor())
						);

		
		DataStream<String> joinDs = nameDs
				.coGroup(infoDs).where(x->x).equalTo(x->x).window(TumblingEventTimeWindows.of(Time.milliseconds(1000)))
				.apply(new RichCoGroupFunction<String, String, String> (){

					@Override
					public void coGroup(Iterable<String> first, Iterable<String> second, Collector<String> out)
							throws Exception {
						Iterator<String> it = first.iterator();
						while(it.hasNext()){
							System.out.println(it.next());
						}
						
						Iterator<String> itsecond = second.iterator();
						while(itsecond.hasNext()){
							System.out.println(itsecond.next());
						}
					}
			    });
				
		joinDs.print();

		env.execute();
	}
	private static class TimeStampExtractor implements TimestampAssigner<String> {
        @Override
        public long extractTimestamp(String element, long recordTimestamp) {
            return System.currentTimeMillis();
        }
    }
	
	private static class MyWatermarkGenerator implements WatermarkGenerator<String>, Serializable {

	    private final long maxTimeLag = 5000; // 5 秒

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
