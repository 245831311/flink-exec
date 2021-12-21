package operator.sideout;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class SideOutOperator {
	
	final static OutputTag<String> sideOutputTag = new OutputTag<String>("test",TypeInformation.of(String.class));
	
	public static void main(String[] args) throws Exception {
		
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<String> socketDs = env.socketTextStream("localhost", 9999);
		
		DataStream<String> sideoutDs = socketDs.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public void flatMap(String value, Collector<String> out) throws Exception {
				String[] str = value.split(",");
				for(String word : str){
					out.collect(word);
				}
			}
		}).process(new ProcessFunction<String, String>() {
			@Override
			public void processElement(String value, Context context,
					Collector<String> out) throws Exception {
				context.output(sideOutputTag, value);
			}
		});
		
		SingleOutputStreamOperator<String> sideOut = (SingleOutputStreamOperator<String>) sideoutDs;
		
		sideOut.getSideOutput(sideOutputTag).print();
		
		env.execute();
	}

}
