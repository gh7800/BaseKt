package cn.shineiot.base.utils;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
 * 下载工具
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

    public static DownLoadTask getInstance() {
        mcontext = BaseApplication.context();
        downLoadTask = new DownLoadTask();
        return downLoadTask;
    }

    public static void cancelTask() {
        if (null != downLoadTask) {
            downLoadTask.cancel(true);
        }
    }


    public static class DownLoadTask extends AsyncTask<String, Integer, File> {

        //private ContentLoadingProgressBar progressBar;
        //private AppCompatTextView tv;
        //private Dialog mDialog;
        private int currentProgress = 0;
        private Boolean isDownload = false;

        @Override
        protected void onPreExecute() {
            if(null != downProgressListener) {
                downProgressListener.downStart();
            }

            //initNotification();

            /*mDialog = new Dialog(mcontext);
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
            mDialog.show();*/

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
            } else if ((value - currentProgress) >= 3) {

                currentProgress = value;

                if(null != downProgressListener) {
                    downProgressListener.downProgress(value);
                }

            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (null != result && isDownFinish) {
                isDownFinish = false;
                if(null != downProgressListener) {
                    downProgressListener.downResult(true,result.getAbsolutePath());
                }
            } else if (null != result && result.exists()) {       //下载失败
                LogUtil.INSTANCE.e("已删除_" + result.getAbsolutePath());
                result.delete();
                if(null != downProgressListener) {
                    downProgressListener.downResult(false,result.getAbsolutePath());
                }
            }
            mcontext = null;
        }

        @Override
        protected void onCancelled(File file) {
            super.onCancelled(file);
            if(null != downProgressListener) {
                downProgressListener.downResult(false,"");
            }
            if (null != file && file.exists()) {
                LogUtil.INSTANCE.e("已删除_" + file.getAbsolutePath());
                file.delete();
            }
        }

        public static DownProgressListener downProgressListener;
        public static void setDownProgressListener(DownProgressListener downProgressListener){
            DownLoadTask.downProgressListener = downProgressListener;
        }
        //取消下载监听。防止内存泄漏
        public static void cancelDownProgressListener(){
            DownLoadTask.downProgressListener = null;
        }

        public interface DownProgressListener {
            void downStart(); //可以进行初始化通知
            void downProgress(int progress); //更新下载进度
            void downResult(boolean result,String path);  //下载结果
        }

    }


}

