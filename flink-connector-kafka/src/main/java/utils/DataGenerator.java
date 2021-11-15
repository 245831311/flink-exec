package utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import entity.PageEvent;

public class DataGenerator extends RichSourceFunction<PageEvent>{

	private int batchTime = 5;
	private int batchNum = 1000;
	private Random random = new Random(20);
	private List<String> pageList = new ArrayList<String>(Arrays.asList("/user","/org","/page","/job","/order"));
	
	@Override
	public void run(SourceContext<PageEvent> ctx)
			throws Exception {
		
		while(true){
			
		}
	}

	@Override
	public void cancel() {
		
	}

	
	public String createUserId(){
		return random.nextInt(100) + "";
	}
	
	public String createTimeStamp(int offerSet){
		Instant instant = Instant.now().plusMillis(offerSet);
		return instant.toEpochMilli() + "";
	}
	
	
}
