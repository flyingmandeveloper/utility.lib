package flyingman.utility.DeviceTool;


import java.text.DecimalFormat;
import java.util.List;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;

public class DeviceTools {

	public static class ScreenResolution {
		public int width = 0;
		public int height = 0;
		public float widthDP = 0.0f;
		public float heightDP = 0.0f;
		public float density = 0.0f;
	}

	/**
	 * 取得設備螢幕上的解淅
	 * 
	 * @param activity
	 *            當下的Activity
	 * 
	 * @return 回傳螢幕的長跟寬，還有密度
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static ScreenResolution getScreenResolution(Activity activity) {
		if (activity == null)
			return null;

		ScreenResolution sr = new ScreenResolution();
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		// display.getWidth() is deprecated
		// display.getHeight() is deprecated
		sr.density = activity.getResources().getDisplayMetrics().density;
		sr.width = outMetrics.widthPixels;
		sr.height = outMetrics.heightPixels;
		sr.widthDP = outMetrics.widthPixels / sr.density;
		sr.heightDP = outMetrics.heightPixels / sr.density;

		return sr;
	}

	/**
	 * dp 轉 pixel
	 * 
	 * @param context
	 *            當下的Context
	 * @param dip
	 *            要轉成pixel的dip值
	 * 
	 * @return 回傳pixel值
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static int getPixelFromDip(Context context, float dip) {
		if (context == null)
			return 0;

		Resources r = context.getResources();

		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, r.getDisplayMetrics());
	}

	/**
	 * pixel 轉 dp
	 * 
	 * @param context
	 *            當下的Context
	 * @param pixel
	 *            要轉成dip的pixel值
	 * 
	 * @return 回傳dip值
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static float convertDensityPixel(Context context, int pixel) {
		if (context == null)
			return 0;
		Resources r = context.getResources();

		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixel,
				r.getDisplayMetrics());
	}

	/**
	 * 取得設備目前是 直立 或 橫向
	 * 
	 * @param activity
	 *            當下的activity
	 * 
	 * @return 回傳dip值
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static int getScreenOrientation(Activity activity) {
		if (activity == null)
			return Configuration.ORIENTATION_UNDEFINED;

		Display getOrient = activity.getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if (getOrient.getWidth() == getOrient.getHeight()) {
			orientation = Configuration.ORIENTATION_SQUARE;
		} else {
			if (getOrient.getWidth() < getOrient.getHeight()) {
				orientation = Configuration.ORIENTATION_PORTRAIT;
			} else {
				orientation = Configuration.ORIENTATION_LANDSCAPE;
			}
		}
		return orientation;
	}

	/**
	 * 取得InputMethodManager
	 * 
	 * @return InputMethodManager
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static InputMethodManager getInputMethodManager(Activity activity) {
		if (activity == null)
			return null;
		return (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/**
	 * 印記憶體使用量的log
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static void logHeap(String st) {
		Double allocated = new Double(Debug.getNativeHeapAllocatedSize())
				/ new Double((1048576));
		Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
		Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		Double totalM = new Double(Runtime.getRuntime().totalMemory())
				/ new Double((1048576));
		Double freeM = new Double(Runtime.getRuntime().freeMemory())
				/ new Double((1048576));
		Double maxM = new Double(Runtime.getRuntime().maxMemory())
				/ new Double((1048576));

		Log.d("debug", "debug. ===============" + st + "==================");
		Log.d("debug", "debug.heap native: allocated " + df.format(allocated)
				+ "MB of " + df.format(available) + "MB (" + df.format(free)
				+ "MB free)");
		Log.d("debug",
				"debug.memory: allocated: " + df.format(totalM) + "/"
						+ df.format(maxM) + " MB " + " used: "
						+ df.format(totalM - freeM) + "MB Free: "
						+ df.format(freeM) + "MB");
		Log.d("debug", "total: " + (allocated + (totalM - freeM)));

	}

	/**
	 * 取得Activity是否正在執行中
	 * 
	 * @param ctx
	 *            當下的Context
	 * 
	 * @return boolean 是/否 正在執行中
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static boolean isActivityRunning(Context ctx) {
		ActivityManager activityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = activityManager
				.getRunningTasks(Integer.MAX_VALUE);

		for (RunningTaskInfo task : tasks) {
			if (ctx.getPackageName().equalsIgnoreCase(
					task.baseActivity.getPackageName()))
				return true;
		}

		return false;
	}

	/**
	 * 取得Activity是否正在執行中
	 * 
	 * @param ctx
	 *            當下的Context
	 * @param activityClass
	 *            要檢查的Activity
	 * 
	 * @return boolean 是/否 正在執行中
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static boolean isActivityRunning(Context ctx, Class activityClass) {
		ActivityManager activityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = activityManager
				.getRunningTasks(Integer.MAX_VALUE);

		for (ActivityManager.RunningTaskInfo task : tasks) {
			if (activityClass.getCanonicalName().equalsIgnoreCase(
					task.baseActivity.getClassName()))
				return true;
		}

		return false;
	}

	/**
	 * 在google play商城上開啟App的下載頁面
	 * 
	 * @param ctx
	 *            當下的Context
	 * @param appPackageName
	 *            App的package名稱
	 * 
	 * @author Jeff
	 * @date 2014-5-22
	 */
	public static void openAppAtGooglePlay(Context ctx, String appPackageName) {
		try {
			ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://play.google.com/store/apps/details?id="
							+ appPackageName)));
		}
	}
	
	

}
