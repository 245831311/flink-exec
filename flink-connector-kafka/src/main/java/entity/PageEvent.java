package entity;

import java.io.Serializable;

public class PageEvent implements Serializable{

	public String timestamp;
	
	public String page;
	
	public String userId;
	
	
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "PageEvent [timestamp=" + timestamp + ", page=" + page + ", userId=" + userId + "]";
	}

	
	
}
