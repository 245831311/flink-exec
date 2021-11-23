package operator.join;

import java.io.Serializable;

import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class JoinOperator {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<String> nameDs = env.fromElements("man","jerry","aka","john","apple")
				.assignTimestampsAndWatermarks(
						WatermarkStrategy
						.forGenerator((ctx) ->new MyWatermarkGenerator())
						.withTimestampAssigner((x)->new TimeStampExtractor())
						);

		DataStream<String> infoDs = env.fromElements("man","vv")
				.assignTimestampsAndWatermarks(
						WatermarkStrategy
						.forGenerator((ctx) ->new MyWatermarkGenerator())
						.withTimestampAssigner((x)->new TimeStampExtractor())
						);
		
		DataStream<String> joinDs = nameDs.join(infoDs)
		.where(x->x)
		.equalTo(x->x)
		.window(TumblingProcessingTimeWindows.of(Time.milliseconds(2)))
		.apply(new JoinFunction<String, String, String> (){
	        @Override
	        public String join(String first, String second) {
	            return first + "," + second;
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
