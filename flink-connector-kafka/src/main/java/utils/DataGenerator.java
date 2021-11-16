package utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import entity.PageEvent;

public class DataGenerator extends RichSourceFunction<PageEvent>{

	private int batchTime = 2 * 1000;
	private int batchNum = 1000;
	private Random random = new Random(20);
	private List<String> pageList = new ArrayList<String>(Arrays.asList("/user","/org","/page","/job","/order"));
	
	@Override
	public void run(SourceContext<PageEvent> ctx)
			throws Exception {
		
		int count = 0;
		while(true){
			PageEvent page = new PageEvent();
			page.setTimestamp(createTimeStamp(count));
			page.setUserId(createUserId());
			page.setPage(getPage());
			
			ctx.collect(page);
			
			count++;
			if(count == batchNum){
				count = 0;
				Thread.sleep(batchTime);
			}
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
	
	public String getPage(){
		return pageList.get(random.nextInt(5));
	}
	
	
}
