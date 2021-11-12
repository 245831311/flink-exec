package connector;

public class PageEvent {

	public String timestamp;
	
	public String page;
	
	
	public PageEvent() {
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "PageEvent [timestamp=" + timestamp + ", page=" + page + "]";
	}
	
}
