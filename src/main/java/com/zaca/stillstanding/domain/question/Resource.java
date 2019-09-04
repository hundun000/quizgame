package com.zaca.stillstanding.domain.question;

public class Resource {
	private final ResourceType type;
	private final String data;
	
	public Resource(String localFilePathName) {
		this.type = ResourceType.getByLocalFileExtension(localFilePathName);
		this.data = localFilePathName;
	}
	
	public String getData() {
        return data;
    }
}
