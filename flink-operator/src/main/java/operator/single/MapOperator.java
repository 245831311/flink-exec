package operator.single;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MapOperator {

	
	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<String> wordDs = env.fromElements("man","jerry","aka","john","apple","man","john");
		
		//wordDs.map(x->x);
		
		/*DataStream<Tuple2<String, Integer>> wordTupleDs = wordDs.map(new MapFunction<String,Tuple2<String, Integer>>(){
			@Override
			public Tuple2<String, Integer> map(String value) throws Exception {
				return Tuple2.of(value, 1);
			}
		});*/
		
		
		DataStream<Tuple2<String, Integer>> wordTupleDs = wordDs.map(new MapFunction2());
		
		wordTupleDs.print("map-");
		
		DataStream<Tuple2<String, Integer>> keyByDs = wordTupleDs.keyBy(0).sum(1);
		
		keyByDs.print("sum-");
		
		DataStream<Tuple2<String, Integer>> reduceDs = wordTupleDs.keyBy(0).reduce(new ReduceFunction<Tuple2<String, Integer>>(){

			@Override
			public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2)
					throws Exception {
				return new Tuple2<String, Integer>(value1.f0,value1.f1+value2.f1);
			}
			
		});
		
		reduceDs.print("reduce-");
		
		env.execute();
	}
	
	
	public static class MapFunction2 extends RichMapFunction<String, Tuple2<String, Integer>>{
		@Override
		public Tuple2<String, Integer> map(String value) throws Exception {
			return Tuple2.of(value, 1);
		}
		
	}
}
