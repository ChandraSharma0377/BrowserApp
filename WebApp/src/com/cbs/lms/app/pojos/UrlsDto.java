package com.cbs.lms.app.pojos;

public class UrlsDto {

	private String url;
	private int uniqueId;

	public UrlsDto() {
	}

	public UrlsDto(String url, int uniqueId) {
		super();
		this.url = url;
		this.uniqueId = uniqueId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public String toString() {
		return "UrlsDto [url=" + url + ", uniqueId=" + uniqueId + "]";
	}

}
