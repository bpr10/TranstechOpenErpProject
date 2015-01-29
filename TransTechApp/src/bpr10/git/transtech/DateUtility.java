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
					Log.d(tag, "Default vaile " + diff);
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

	public String notificationDate(Date date) {
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
				Log.d("Notification time", "" + diff);
				switch (diff) {

				case 0:
					return "" + String.valueOf(dateToChange.get(Calendar.HOUR))
							+ ":" + min + " " + am_pm;

				case 1:
					return "Yesterday";

				default:
					return ""
							+ dateToChange.get(Calendar.DAY_OF_MONTH)
							+ " "
							+ dateToChange.getDisplayName(Calendar.MONTH,
									Calendar.SHORT, Locale.US);

				}

			} else {
				return ""
						+ dateToChange.get(Calendar.DAY_OF_MONTH)
						+ " "
						+ dateToChange.getDisplayName(Calendar.MONTH,
								Calendar.SHORT, Locale.US) + " ";
			}

		} else {
			return ""
					+ dateToChange.get(Calendar.DAY_OF_MONTH)
					+ " "
					+ dateToChange.getDisplayName(Calendar.MONTH,
							Calendar.SHORT, Locale.US) + " "
					+ dateToChange.get(Calendar.YEAR);
		}

	}

	public String makeExpiryDate(Date date) {
		Calendar todayDate = Calendar.getInstance();
		Calendar dateToChange = Calendar.getInstance();
		dateToChange.setTime(date);
		if ((todayDate.get(Calendar.YEAR)) == (dateToChange.get(Calendar.YEAR))) {
			if ((todayDate.get(Calendar.MONTH)) == (dateToChange
					.get(Calendar.MONTH))) {
				int diff = (todayDate.get(Calendar.DAY_OF_MONTH))
						- (dateToChange.get(Calendar.DAY_OF_MONTH));
				switch (diff) {

				case 0:
					return "Expires Today";

				case 1:
					return "Expired Yesterday";

				case -1:
					return "Expires Tomorrow";
				default:
					Log.d(tag, "Default vaile " + diff);
					if (diff < 0) {
						return "Expires in " + Math.abs(diff) + "days";
					} else {
						return "Expired on "
								+ dateToChange.get(Calendar.DAY_OF_MONTH)
								+ " "
								+ dateToChange.getDisplayName(Calendar.MONTH,
										Calendar.SHORT, Locale.US);
					}
				}

			} else {
				return ""
						+ dateToChange.get(Calendar.DAY_OF_MONTH)
						+ " "
						+ dateToChange.getDisplayName(Calendar.MONTH,
								Calendar.SHORT, Locale.US) + " ";
			}

		} else {
			return ""
					+ dateToChange.get(Calendar.DAY_OF_MONTH)
					+ " "
					+ dateToChange.getDisplayName(Calendar.MONTH,
							Calendar.SHORT, Locale.US) + " "
					+ dateToChange.get(Calendar.YEAR);
		}
	}
}
