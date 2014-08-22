package flyingman.utility.DeviceTool;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
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

	/**
	 * 判斷是否有開啟定位系統
	 * 
	 * @param context
	 *            當下的Context
	 * 
	 * @author Jeff
	 * @date 2014-06-10
	 */
	public static boolean isLocationEnable(Context context) {
		LocationManager lm = null;
		boolean gps_enabled, network_enabled;
		boolean enable = false;
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			enable = gps_enabled || network_enabled;
		} catch (Exception ex) {
		}

		return enable;

	}

	public static interface IOpenLocationSettingDailogOnClick
	{
		public void onPositiveClick(DialogInterface dialog, int which);
		public void onNegativeClick(DialogInterface dialog, int which);
	}
	
	/**
	 * 開啟設定的定位設定介面
	 * 
	 * @param activity
	 *            當下的activity
	 * @param requestCode
	 *            當以startActivityForResult開啟Setting Activity 
	 *            可自訂 requestCode值。       
	 * @param titleId
	 *            跳出Dialog時的設定title ResourseID。            
	 * @param msgId
	 *            跳出Dialog時的設定message ResourseID。            
	 * @param posTilteId
	 *            跳出Dialog時的設定正向按鈕的 ResourseID。            
	 * @param NegTitleId
	 *            跳出Dialog時的設定負向按鈕的 ResourseID。 
	 * @author Jeff
	 * @date 2014-06-12
	 */
	public static void openLocationSettingDailog(final Activity activity,final int requestCode, 
			int titleId, int msgId, int posTilteId, int NegTitleId)
	{
		String title = null;
		String msg = null;
		String posTilte = null;
		String NegTitle = null;
		if (titleId != 0)
			title = activity.getString(titleId);

		if (msgId != 0)
			msg = activity.getString(msgId);

		if (posTilteId != 0)
			posTilte = activity.getString(posTilteId);

		if (NegTitleId != 0)
			NegTitle = activity.getString(NegTitleId);
		openLocationSettingDailog(
				activity,
				requestCode,
				title,
				msg,
				posTilte,
				NegTitle);
	}
	

	/**
	 * 開啟設定的定位設定介面
	 * 
	 * @param activity
	 *            當下的activity
	 * @param requestCode
	 *            當以startActivityForResult開啟Setting Activity 
	 *            可自訂 requestCode值。       
	 * @param titleId
	 *            跳出Dialog時的設定title ResourseID。            
	 * @param msgId
	 *            跳出Dialog時的設定message ResourseID。            
	 * @param posTilteId
	 *            跳出Dialog時的設定正向按鈕的 ResourseID。            
	 * @param NegTitleId
	 *            跳出Dialog時的設定負向按鈕的 ResourseID。
	 * @param onClickListener
	 *           Dialog時的設定正向/負向按鈕的callback函式。
	 * @author Jeff
	 * @date 2014-06-12
	 */
	public static void openLocationSettingDailog(final Activity activity,final int requestCode, 
			int titleId, int msgId, int posTilteId, int NegTitleId,
			IOpenLocationSettingDailogOnClick onClickListener)
	{
		String title = null;
		String msg = null;
		String posTilte = null;
		String NegTitle = null;
		if (titleId != 0)
			title = activity.getString(titleId);

		if (msgId != 0)
			msg = activity.getString(msgId);

		if (posTilteId != 0)
			posTilte = activity.getString(posTilteId);

		if (NegTitleId != 0)
			NegTitle = activity.getString(NegTitleId);
		openLocationSettingDailog(
				activity,
				requestCode,
				title,
				msg,
				posTilte,
				NegTitle,onClickListener);
	}
	
	/**
	 * 開啟設定的定位設定介面
	 * 
	 * @param activity
	 *            當下的activity
	 * @param requestCode
	 *            當以startActivityForResult開啟Setting Activity 
	 *            可自訂 requestCode值。       
	 * @param title
	 *            跳出Dialog時的設定title String。            
	 * @param msg
	 *            跳出Dialog時的設定message String。            
	 * @param posTilte
	 *            跳出Dialog時的設定正向按鈕的 String。            
	 * @param negTitle
	 *            跳出Dialog時的設定負向按鈕的 String。
	 * @author Jeff
	 * @date 2014-06-12
	 */
	public static void openLocationSettingDailog(final Activity activity,final int requestCode, 
			String title, String msg, String posTilte, String negTitle)
	{
		openLocationSettingDailog(activity,requestCode,title,msg,posTilte,negTitle,null);
	}

	/**
	 * 開啟設定的定位設定介面
	 * 
	 * @param activity
	 *            當下的activity
	 * @param requestCode
	 *            當以startActivityForResult開啟Setting Activity 
	 *            可自訂 requestCode值。       
	 * @param title
	 *            跳出Dialog時的設定title String。            
	 * @param msg
	 *            跳出Dialog時的設定message String。            
	 * @param posTilte
	 *            跳出Dialog時的設定正向按鈕的 String。            
	 * @param negTitle
	 *            跳出Dialog時的設定負向按鈕的 String。
	 * @param onClickListener
	 *           Dialog時的設定正向/負向按鈕的callback函式。              
	 * @author Jeff
	 * @date 2014-06-12
	 */
	public static void openLocationSettingDailog(final Activity activity,final int requestCode, 
			String title, String msg, String posTilte, String negTitle,
			final IOpenLocationSettingDailogOnClick onClickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		if (title != null)
			builder.setTitle(title);

		if (msg != null)
			builder.setMessage(msg);

		if (posTilte != null)
		{

			builder.setPositiveButton(posTilte, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					activity.startActivityForResult(
							new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
							requestCode);

					if (onClickListener != null)
						onClickListener.onPositiveClick(dialog, which);
				}
			});

		}

		if (negTitle != null)
		{

			builder.setNegativeButton(negTitle, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (onClickListener != null)
						onClickListener.onNegativeClick(dialog, which);
				}
			});

		}

		builder.setCancelable(true);

		AlertDialog alert = builder.create();
		alert.show();
	}
	
    public static interface IDailogOnClick
    {
        public void onPositiveClick(DialogInterface dialog, int which);
        public void onNegativeClick(DialogInterface dialog, int which);
    }
 
	public static AlertDialog createAlertDialog(final Activity activity, 
            String title, String msg, String posTilte, String negTitle,
            final IDailogOnClick onClickListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if (title != null)
            builder.setTitle(title);

        if (msg != null)
            builder.setMessage(msg);

        if (posTilte != null)
        {

            builder.setPositiveButton(posTilte, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onClickListener != null)
                        onClickListener.onPositiveClick(dialog, which);
                }
            });

        }

        if (negTitle != null)
        {

            builder.setNegativeButton(negTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onClickListener != null)
                        onClickListener.onNegativeClick(dialog, which);
                }
            });

        }

        builder.setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
        
        return alert;
    }
	
	
	/**
	 * 取得狀態Bar的高度
	 * 
	 * @param activity
	 *            當下的activity
	 * @author Jeff
	 * @date 2014-06-18
	 */	
	public static int getStatusBarHeight(Activity activity) { 
	      int result = 0;
	      if (activity == null)
	    	  return result;
	      
	      int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = activity.getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	} 

	/**
	 * 取得Action Bar的高度
	 * 
	 * @param activity
	 *            當下的activity
	 * @author Jeff
	 * @date 2014-06-18
	 */	
	public static int getActionBarHeight(Activity activity)
	{
		TypedValue tv = new TypedValue();
		if (activity!=null && activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		    return TypedValue.complexToDimensionPixelSize(tv.data,activity.getResources().getDisplayMetrics());
		else
			return 0;
	}
	
	/**
	 * 判斷現在是否有連線狀態
	 * 
	 * @param context
	 *            當下的Context
	 * @author Jeff
	 * @date 2014-07-24
	 */	
	public static boolean hasConnection(Context context) {
		if (context == null)
			return false;
		
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
	        Context.CONNECTIVITY_SERVICE);

	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected()) {
	      return true;
	    }

	    return false;
	  }
	
	/**
     * 開啟網頁瀏覽器
     * 
     * @param context
     *            當下的Context
     * @param url 要開啟的URL 如：　"http://google.com"       
     * @author Jeff
     * @date 2014-08-4
     */ 
	public static void openWebBrowser(Context context,String url) {
	    if (context ==null || url == null || (url!=null&&url.contains("http") == false))
	    {
	        Log.e("DeviceTool", "openWebBrowser error param");
	    }

	  Uri uri = Uri.parse(url);  
	  Intent it = new Intent(Intent.ACTION_VIEW, uri);  
	  context.startActivity(it);
	}
	
	/**
     * 開啟撥號程式
     * 
     * @param context
     *            當下的Context
     * @param url 要撥打的電話 如：　"0412345678"       
     * @author Jeff
     * @date 2014-08-4
     */ 
	public static void openPhoneActivity(Context context,String url) {
        if (context ==null || url == null)
        {
            Log.e("DeviceTool", "openPhoneActivity error param");
        }

        Uri uri = Uri.parse("tel:"+url);  
        Intent it = new Intent(Intent.ACTION_DIAL, uri);  
        context.startActivity(it);  
    }

	/**
     * 直接撥號程式
     * 
     * @param context
     *            當下的Context
     * @param url 要撥打的電話 如：　"0412345678"       
     * @author Jeff
     * @date 2014-08-4
     */ 
    public static void callPhoneActivity(Context context,String url) {
        if (context ==null || url == null)
        {
            Log.e("DeviceTool", "openPhoneActivity error param");
        }

        Uri uri = Uri.parse("tel:"+url);  
        Intent it = new Intent(Intent.ACTION_CALL, uri);  
        context.startActivity(it);  
    }
	
	/**
     * 開啟email
     * 
     * @param context
     *            當下的Context
     * @param url 收件人信箱
     * @param subject 信件標頭
     * @param body 信件內容      
     * @author Jeff
     * @date 2014-08-4
     */ 
    public static void openEmailSender(Context context,String url,String subject,String body) {
        if (context ==null || url == null)
        {
            Log.e("DeviceTool", "openEmailSender error param");
        }

        Intent intent =new Intent(Intent.ACTION_VIEW);
        Uri data =Uri.parse("mailto:"+url+"?subject="+ subject +"&body="+ body);
        intent.setData(data);
        context.startActivity(intent);
    }
	 

    /**
     * 讀取本機圖檔(有縮小圖檔功能)
     * 
     * @param picturePath 讀取本機圖檔路徑
     * @param width 最小圖 寬
     * @param height 最小圖 高
     * @author Jeff
     * @date 2014-08-22
     */ 
    
    public static Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);
     
        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);
     
        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;
     
        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    } 
     
    static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image 
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
     
        if (height > reqHeight || width > reqWidth) {
     
            // Calculate ratios of height and width to requested height and 
            // width 
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
     
            // Choose the smallest ratio as inSampleSize value, this will 
            // guarantee 
            // a final image with both dimensions larger than or equal to the 
            // requested height and width. 
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        } 
     
        return inSampleSize;
    } 

}