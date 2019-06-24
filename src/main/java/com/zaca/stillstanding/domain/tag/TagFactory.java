package com.zaca.stillstanding.domain.tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TagFactory {
	private static Map<String, Tag> tags = new HashMap<>();
	
	
	public static void registerTag(String name) {
		Tag tag = new Tag(name);
		tags.put(name, tag);
	}
	
	public static Tag getTag(String name) {
		return tags.get(name);
	}
	
	public static Collection<Tag> getTags() {
		return tags.values();
	}
	
}
