package operator.single;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

public class MapOperatorDataSet {

	
	public static void main(String[] args) throws Exception {
		
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		
		DataSet<String> wordDs = env.fromElements("man","jerry","aka","john","apple","man","john");
		
		//wordDs.map(x->x);
		
		/*DataStream<Tuple2<String, Integer>> wordTupleDs = wordDs.map(new MapFunction<String,Tuple2<String, Integer>>(){
			@Override
			public Tuple2<String, Integer> map(String value) throws Exception {
				return Tuple2.of(value, 1);
			}
		});*/
		
		
		DataSet<Tuple2<String, Integer>> wordTupleDs = wordDs.map(new MapFunction2());
		
		wordTupleDs.print("map-");
		
		DataSet<Tuple2<String, Integer>> keyByDs = wordTupleDs.groupBy(0).sum(1);
		
		keyByDs.print("keyBy-");
		
		env.execute();
	}
	
	
	public static class MapFunction2 extends RichMapFunction<String, Tuple2<String, Integer>>{
		@Override
		public Tuple2<String, Integer> map(String value) throws Exception {
			return Tuple2.of(value, 1);
		}
		
	}
}
