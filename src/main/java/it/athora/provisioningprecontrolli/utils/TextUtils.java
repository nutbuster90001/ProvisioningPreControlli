package it.athora.provisioningprecontrolli.utils;

import java.util.Collection;

public final class TextUtils {

	private TextUtils() {

	}

	public static boolean isNotBlank(String s) {
		return s != null && !s.trim().isEmpty();
	}

	public static boolean isBlank(String s) {
		return !isNotBlank(s);
	}
	
	public static boolean allNotBlank(Collection<String> strings) {
		return strings != null && strings.stream().allMatch(TextUtils::isNotBlank);
	}

}
