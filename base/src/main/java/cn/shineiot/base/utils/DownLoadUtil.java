package cn.shineiot.base.utils;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shineiot.base.BaseApplication;
import cn.shineiot.base.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author zhang
 */

public class DownLoadUtil {

    private static DownLoadTask downLoadTask;
    private static Context mcontext;
    public static OkHttpClient okhttpclient = null;
    private static Boolean isDownFinish = false;

    public static OkHttpClient getOkHttp() {
        if (okhttpclient == null) {
            okhttpclient = new OkHttpClient();
        }
        return okhttpclient;
    }

    public static DownLoadTask getInstance(Context context) {
        mcontext = context;
        downLoadTask = new DownLoadTask();
        return downLoadTask;
    }

    public static void cancelTask() {
        if (null != downLoadTask) {
            downLoadTask.cancel(true);
        }
    }


    public static class DownLoadTask extends AsyncTask<String, Integer, File> {

        private NotificationManager notificationManager;
        private Notification notification;
        private NotificationCompat.Builder builder;
        private NotificationCompat.Builder builder2;
        private ContentLoadingProgressBar progressBar;
        private AppCompatTextView tv;
        private Dialog mDialog;
        private int currentProgress = 0;
        private Boolean isDownload = false;

        //初始化通知
        private void initNotification() {
            notificationManager = (NotificationManager) BaseApplication.context().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                List<NotificationChannel> list = new ArrayList<NotificationChannel>();
                NotificationChannel channel = new NotificationChannel("MyChannelId3", "Download", NotificationManager.IMPORTANCE_LOW);
                channel.enableVibration(false);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                channel.setGroup("MyGroupId");

                NotificationChannel channel2 = new NotificationChannel("MyChannelId4", "DownloadMessage", NotificationManager.IMPORTANCE_HIGH);
                channel2.enableVibration(false);
                channel2.setGroup("MyGroupId");

                list.add(channel);
                list.add(channel2);
                notificationManager.createNotificationChannels(list);

                builder = new NotificationCompat.Builder(mcontext, "MyChannelId3");
                builder2 = new NotificationCompat.Builder(mcontext, "MyChannelId4");
            } else {
                builder = new NotificationCompat.Builder(BaseApplication.context(), null);
                builder2 = new NotificationCompat.Builder(mcontext, null);
            }

            builder.setContentTitle("正在更新...") //设置通知标题
                    .setSmallIcon(R.drawable.icon_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(BaseApplication.context().getResources(), R.drawable.icon_logo)) //设置通知的大图标
                    .setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
                    .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知的优先级：最大
                    .setAutoCancel(false)//设置通知被点击一次是否自动取消
                    .setContentText("下载进度:" + "0%")
                    .setProgress(100, 0, false);

            builder2.setContentText("移动办公正在下载...")
                    .setSmallIcon(R.drawable.icon_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(BaseApplication.context().getResources(), R.drawable.icon_logo)) //设置通知的大图标
                    .setContentTitle("版本更新")
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setAutoCancel(true);

            notification = builder.build();//构建通知对象
            notificationManager.notify(1, notification);

            notificationManager.notify(2,builder2.build());

        }

        @Override
        protected void onPreExecute() {
            initNotification();

            mDialog = new Dialog(mcontext);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_update, null);
            mDialog.setContentView(view);
            tv = view.findViewById(R.id.updateTv);
            progressBar = view.findViewById(R.id.updateProgressBar);
            Window dialogWindow = mDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (ScreenUtil.INSTANCE.getScreenWidth() * 0.95); // 宽度设置为屏幕的1
            lp.alpha = 1f;//完全不透明
            dialogWindow.setAttributes(lp);
            mDialog.setCanceledOnTouchOutside(false);
//            mDialog.show();

        }

        @Override
        protected File doInBackground(String... params) {

            OkHttpClient okhttpClient = getOkHttp();
            Request request = new Request.Builder().url(params[0]).build();

            final String filePath = params[1];
            final String fileName = params[2];

            Response response;
            File result = null;
            try {
                response = okhttpClient.newCall(request).execute();
                if (response.isSuccessful()) {

                    File dir = new File(filePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    InputStream is;
                    FileOutputStream fos;
                    is = response.body().byteStream();
                    long file_length = response.body().contentLength();

                    if (file_length <= 0) {
                        return null;
                    }
                    result = new File(dir, fileName);
                    LogUtil.INSTANCE.e(result.getAbsolutePath());

                    int total_length = 0;
                    int len;
                    byte[] buf = new byte[1024];

                    fos = new FileOutputStream(result);
                    while ((len = is.read(buf)) != -1) {
                        total_length += len;
                        int progress_value = (int) ((total_length / (float) file_length) * 100);
                        fos.write(buf, 0, len);
                        publishProgress(progress_value);

                    }
                    fos.flush();

                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        fos.close();
                    } catch (IOException ignored) {
                    }
                } else {
                    LogUtil.INSTANCE.e(response.message());
                }

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int value = values[0];

            if (value == 100) {
                isDownFinish = true;
            } else if ((value - currentProgress) >= 5) {
                if(!isDownload && value >= 20) {
                    notificationManager.cancel(2);
                    isDownload = true;
                }

                currentProgress = value;
                tv.setText("下载中.. " + value + "%");
                progressBar.setProgress(value);

//                RemoteViews remoteViews = new RemoteViews(mcontext.getPackageName(),R.layout.dialog_update);
//                remoteViews.setProgressBar(R.id.updateProgressBar,100,value,false);
//                remoteViews.setTextViewText(R.id.updateTv,"下载进度:" + value + "%");
//                notification.contentView = remoteViews;

                builder.setProgress(100, value, false);
                builder.setContentText("下载进度:" + value + "%");

                notification = builder.build();
                notificationManager.notify(1, notification);
            }
        }

        @Override
        protected void onPostExecute(File result) {
            mDialog.dismiss();
            notificationManager.cancel(1);

            if (null != result && isDownFinish) {
                isDownFinish = false;
                downProgressListener.downResult(true,result.getAbsolutePath());
                //ScreenUtil.INSTANCE.installApk(mcontext, result);
            } else if (null != result && result.exists()) {       //下载失败
                LogUtil.INSTANCE.e("已删除_" + result.getAbsolutePath());
                result.delete();
                downProgressListener.downResult(false,result.getAbsolutePath());
            }
            mcontext = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //notificationManager.cancel(1);
        }

        @Override
        protected void onCancelled(File file) {
            super.onCancelled(file);
            notificationManager.cancel(1);
            if (null != file && file.exists()) {
                LogUtil.INSTANCE.e("已删除_" + file.getAbsolutePath());
                file.delete();
            }
        }

        public static DownProgressListener downProgressListener;
        public static void setDownProgressListener(DownProgressListener downProgressListener){
            DownLoadTask.downProgressListener = downProgressListener;
        }
        public static void unsetDownProgressListener(){
            DownLoadTask.downProgressListener = null;
        }
        public interface DownProgressListener {
            void downResult(boolean result,String path); //下载结果
        }

    }


}

