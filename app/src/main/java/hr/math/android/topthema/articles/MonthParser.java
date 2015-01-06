package hr.math.android.topthema.articles;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonthParser {
	private static Map<String, String> monthMap;
	private static MonthParser mm = new MonthParser();

	private MonthParser() {
		monthMap = new HashMap<String, String>();
		monthMap.put("Januar", "01");
		monthMap.put("Februar", "02");
		monthMap.put("März", "03");
		monthMap.put("April", "04");
		monthMap.put("Mai", "05");
		monthMap.put("Juni", "06");
		monthMap.put("Juli", "07");
		monthMap.put("August", "08");
		monthMap.put("September", "09");
		monthMap.put("Oktober", "10");
		monthMap.put("November", "11");
		monthMap.put("Dezember", "12");
	}

	public static String get(String monthName) {
		return monthMap.get(monthName);
	}

	public static String getFullDate(String date) {
		String[] atoms = date.split(" ");
		atoms[1] = monthMap.get(atoms[1]);
		return atoms[0] + " " + atoms[1] + " " + atoms[2];
	}

	public static String parseDate(String s) {
		String dateString = null;

		try {
		Matcher dateMatcher = Pattern.compile("[0-9]+(\\.)*( )*[A-Zäa-z]+(\\.)*( )*[0-9]+").matcher(s);
		if (dateMatcher.find()) {
			String fullDate = dateMatcher.group(0);
			Matcher dayMatcher = Pattern.compile("[0-9]+").matcher(fullDate);
			Matcher monthMatcher = Pattern.compile("[A-Zäa-z]+").matcher(fullDate);
			Matcher yearMatcher = Pattern.compile("[0-9]{4}").matcher(fullDate);

			dayMatcher.find();
			monthMatcher.find();
			yearMatcher.find();

			String day = dayMatcher.group(0);
			String month = monthMatcher.group(0);
			String year = yearMatcher.group(0);
			dateString = day + " " + monthMap.get(month) + " " + year;
		} else {
			dateMatcher = Pattern.compile("[0-9]+(\\.)*( )*[0-9]+(\\.)*( )*[0-9]+").matcher(s);
			if (dateMatcher.find()) {
				dateString = dateMatcher.group(0).replace(".", " ").replace(" +", " ");
			} else {
				return null;
			}
		}


		} catch (IllegalStateException e) {
			System.out.println();
		}

		return dateString;
	}
}
