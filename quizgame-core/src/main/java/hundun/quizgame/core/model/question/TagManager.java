package hundun.quizgame.core.model.question;

import java.util.HashSet;
import java.util.Set;

public class TagManager {
	
	private static Set<String> tags = new HashSet<>();
	
	public static void addTag(String tag) {
		tags.add(tag);
	}
	
	public static Set<String> getTags() {
		return tags;
	}
	
	public static boolean tagExsist(String tag) {
		return tags.contains(tag);
	}

}
