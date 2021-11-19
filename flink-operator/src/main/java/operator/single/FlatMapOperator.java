package operator.single;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FlatMapOperator {

	
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<String> wordDs = env.fromElements("hello jennry i am the one guy who has many girlFriend that "
				+ "you can love mo once more again and i will take you to the moon that you never arrive ever before");
		DataStream<Tuple2<String,Integer>> wordFlatMapDs = wordDs.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
			@Override
			public void flatMap(String value, Collector<Tuple2<String,Integer>> out) throws Exception {
				String[] list = value.split(" ");
				for(String str : list){
					out.collect(new Tuple2(str,1));
				}
			}
		});
		env.execute();
	}
	
	
}
