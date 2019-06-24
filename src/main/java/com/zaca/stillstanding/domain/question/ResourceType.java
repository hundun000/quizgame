package com.zaca.stillstanding.domain.question;

public enum ResourceType {
	LOCAL_IMAGE,
	LOCAL_VOICE,
	NONE;
	
	public static ResourceType getByLocalFileExtension(String name) {
		if (name.endsWith(".jpg") || name.endsWith(".png")) {
			return ResourceType.LOCAL_IMAGE;
		}
		
		if (name.endsWith(".Ogg")) {
			return ResourceType.LOCAL_VOICE;
		}
		
		return ResourceType.NONE;
	}

}
