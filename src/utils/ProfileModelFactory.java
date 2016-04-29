package utils;

import android.app.Activity;
import android.content.Context;
import model.InitProfileModel.MontainYelloSoilOne;

public class ProfileModelFactory {
	
	public static MontainYelloSoilOne getMontainYelloSoilOne(Activity activity, int viewId) {
		return (MontainYelloSoilOne) activity.findViewById(viewId);
	}
}
