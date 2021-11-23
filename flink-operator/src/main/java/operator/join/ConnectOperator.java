package operator.join;

import java.io.Serializable;

import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class ConnectOperator {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<String> nameDs = env.fromElements("man","jerry","aka","john","apple","cc").keyBy(x->x);

		DataStream<String> infoDs = env.fromElements("apple","vv").keyBy(x -> x);

				
		
		DataStream<String> joinDs = nameDs
				.connect(infoDs)
				.map(new RichCoMapFunction<String, String, String>() {
					ValueState<String> state;
					@Override
					public String map1(String value) throws Exception {
						String otherValue = state.value();
						if(otherValue != null){
							if(value.equals(otherValue)){
								return "yes";
							}
						}else{
							state.update(value);
						}
						return "no";
					}

					@Override
					public void open(Configuration parameters) throws Exception {
						ValueStateDescriptor<String> vd = new ValueStateDescriptor<>("state", Types.STRING);
						state = getRuntimeContext().getState(vd);
					}

					@Override
					public String map2(String value) throws Exception {
						String otherValue = state.value();
						if(otherValue != null){
							if(value.equals(otherValue)){
								return "yes";
							}
						}else{
							state.update(value);
						}
						return "no";
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
