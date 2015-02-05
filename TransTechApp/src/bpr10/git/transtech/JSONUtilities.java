package bpr10.git.transtech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONUtilities {
	private String tag = getClass().getSimpleName();

	public <T> JSONArray sortJSONArray(JSONArray jsonArray,
			final String sortFiled_key, final T dataType) {
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				jsonObjects.add(jsonArray.getJSONObject(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(jsonObjects, new Comparator<JSONObject>() {

			@Override
			public int compare(JSONObject leftObj, JSONObject rightObj) {
				if (sortFiled_key.equals("visit_time")) {
					try {
						Long leftObjVisitTime = new DateUtility().makeDate(
								leftObj.getString("visit_time"))
								.getTimeInMillis();
						Long rightObjVisitTime = new DateUtility().makeDate(
								rightObj.getString("visit_time"))
								.getTimeInMillis();
						return leftObjVisitTime.compareTo(rightObjVisitTime);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (dataType instanceof String) {
					try {
						String lid = leftObj.getString("comment_id");
						String rid = rightObj.getString("comment_id");
						return lid.compareTo(rid);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (dataType instanceof Double) {
					try {
						Double lid = (Double) leftObj.get(sortFiled_key);
						Double rid = (Double) rightObj.get(sortFiled_key);
						return lid.compareTo(rid);
					} catch (JSONException e) {
						e.printStackTrace();
						Log.d(tag, "sortFiled_key not present " + sortFiled_key);
						return 1;
					}
				}
				return 0;
			}
		});
		return new JSONArray(jsonObjects);
	}
}
