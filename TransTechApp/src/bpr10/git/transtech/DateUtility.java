package bpr10.git.transtech;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.net.ParseException;
import android.util.Log;

public class DateUtility {

	private String tag = "DateUtility";

	public DateUtility() {

	}

	public Calendar makeDate(String serverDate) {
		SimpleDateFormat sourceFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		try {
			Date parsedDate = (Date) sourceFormat.parse(serverDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsedDate);
			return cal;
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Date convertSerevrDatetoLocalDate(String serverDate)
			throws java.text.ParseException {

		SimpleDateFormat sourceFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date parsed = null;
		try {
			parsed = (Date) sourceFormat.parse(serverDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone(cal.getTimeZone().toString());
		SimpleDateFormat destFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy");
		destFormat.setTimeZone(tz);
		Date resultDate = null;
		try {
			resultDate = (Date) destFormat.parse(parsed.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return resultDate;
	}

	public Date makeDateFromLocalDateString(String localDate)
			throws java.text.ParseException {

		SimpleDateFormat sourceFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy");
		Date parsed = null;
		try {
			parsed = (Date) sourceFormat.parse(localDate);
		} catch (ParseException e1) {
			Log.e("LocalDate conversion error", localDate);
		}
		return parsed;
	}

	public String getFriendlyDateString(Date date) {
		Calendar todayDate = Calendar.getInstance();
		Calendar dateToChange = Calendar.getInstance();
		dateToChange.setTime(date);
		String am_pm;
		if (dateToChange.get(Calendar.AM_PM) == 0)
			am_pm = "AM";
		else
			am_pm = "PM";
		String min;
		if (dateToChange.get(Calendar.MINUTE) < 10) {
			min = "0" + dateToChange.get(Calendar.MINUTE);
		} else {
			min = "" + dateToChange.get(Calendar.MINUTE);
		}
		if ((todayDate.get(Calendar.YEAR)) == (dateToChange.get(Calendar.YEAR))) {
			if ((todayDate.get(Calendar.MONTH)) == (dateToChange
					.get(Calendar.MONTH))) {
				int diff = (todayDate.get(Calendar.DAY_OF_MONTH))
						- (dateToChange.get(Calendar.DAY_OF_MONTH));
				switch (diff) {

				case 0:
					return "today at "
							+ String.valueOf(dateToChange.get(Calendar.HOUR))
							+ ":" + min + " " + am_pm;
				case 1:
					return "Yesterday at "
							+ String.valueOf(dateToChange.get(Calendar.HOUR))
							+ ":" + min + " " + am_pm;
				case -1:
					return "Tomorrow";

				default:
					Log.d(tag, "Default value " + diff);
					if (diff < 0) {
						return "in " + Math.abs(diff) + "days";
					} else {
						return "on "
								+ dateToChange.get(Calendar.DAY_OF_MONTH)
								+ " "
								+ dateToChange.getDisplayName(Calendar.MONTH,
										Calendar.SHORT, Locale.US)
								+ " at "
								+ String.valueOf(dateToChange
										.get(Calendar.HOUR)) + ":" + min + " "
								+ am_pm;
					}
				}

			} else {
				return "on "
						+ dateToChange.get(Calendar.DAY_OF_MONTH)
						+ " "
						+ dateToChange.getDisplayName(Calendar.MONTH,
								Calendar.SHORT, Locale.US) + " ";
			}

		} else {
			return "on "
					+ dateToChange.get(Calendar.DAY_OF_MONTH)
					+ " "
					+ dateToChange.getDisplayName(Calendar.MONTH,
							Calendar.SHORT, Locale.US) + " "
					+ dateToChange.get(Calendar.YEAR);
		}

	}

}
