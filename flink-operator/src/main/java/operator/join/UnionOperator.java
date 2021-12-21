package operator.join;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class UnionOperator {

	public static void main(String[] args) throws Exception {
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		DataStream<String> nameDs = env.fromElements("man","jerry","aka","john","apple","cc");

		DataStream<String> infoDs = env.fromElements("apple","vv");

		
		DataStream<String> joinDs = nameDs
				.union(infoDs)
				.map(x->x+"-suffix");
				
		joinDs.print();

		env.execute();
	}
	
}
