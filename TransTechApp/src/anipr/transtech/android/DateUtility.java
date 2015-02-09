package anipr.transtech.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

	public String getFriendlyDateString(Calendar dateToChange) {
		Calendar todayDate = Calendar.getInstance();
		if ((todayDate.get(Calendar.YEAR)) == (dateToChange.get(Calendar.YEAR))) {
			if ((todayDate.get(Calendar.MONTH)) == (dateToChange
					.get(Calendar.MONTH))) {
				int diff = (todayDate.get(Calendar.DAY_OF_MONTH))
						- (dateToChange.get(Calendar.DAY_OF_MONTH));
				switch (diff) {

				case 0:
					return "Today";
				case 1:
					return "Yesterday";
				case -1:
					return "Tomorrow";

				default:
					Log.d(tag, "Default value " + diff);
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
