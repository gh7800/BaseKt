package cn.shineiot.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author GF63
 */
public class ImageUtil {

    public static String compressImage(Context context, String srcPath, String destPath, int maxW, int maxH) throws Exception {
        String imageFile = "";
        try {
            File f = new File(srcPath);
            BitmapFactory.Options newOpts = getSizeOpt(f, maxW, maxH);
            InputStream is = new FileInputStream(f);
            String type = srcPath.substring(srcPath.lastIndexOf(".") + 1);
            if ("png".equals(type)) {
                imageFile = CompressPngFile(context, srcPath, is, newOpts, destPath);
            } else {
                imageFile = CompressJpgFile(context, srcPath, is, newOpts, destPath);
            }
            if (is != null) {
                is.close();
            }
            if (srcPath != null) {
                new File(srcPath).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return imageFile;
    }

    private static String CompressJpgFile(Context c, String oldPath, InputStream is, BitmapFactory.Options newOpts, String filePath) throws Exception {
        Bitmap destBm = BitmapFactory.decodeStream(is, null, newOpts);
        destBm = rotateBitmapToExifOrientation(c, oldPath, destBm);

        return CompressJpgFile(destBm, newOpts, filePath);
    }

    private static String CompressJpgFile(Bitmap destBm, BitmapFactory.Options newOpts, String filePath) throws Exception {
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        if (destBm == null) {
            return null;
        } else {
            File destFile = createNewFile(filePath);

            // 创建文件输出流
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                // 存储
                byte[] temp = new byte[1024];
                //添加时间水印
                //Bitmap newbm = addTimeFlag(destBm);

                int rate = 90;
                destBm.compress(Bitmap.CompressFormat.JPEG, rate, os);
                os.close();
                Log.e("ImageUtil", "file size :" + destFile.length() / 1024 + "k");
                return filePath;
            } catch (Exception e) {
                filePath = null;
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }


    public static File createNewFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        File newFile = new File(filePath);
        try {
            if (!newFile.exists()) {
                int slash = filePath.lastIndexOf('/');
                if (slash > 0 && slash < filePath.length() - 1) {
                    String dirPath = filePath.substring(0, slash);
                    File destDir = new File(dirPath);
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }
                }
            } else {
                newFile.delete();
            }
            newFile.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return newFile;
    }

    /**
     * 先压缩图片大小
     *
     * @return
     * @throws IOException
     */
    public static BitmapFactory.Options getSizeOpt(File file, int maxWidth, int maxHeight) throws IOException {
        // 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        InputStream is = new FileInputStream(file);
        double ratio = getOptRatio(is, maxWidth, maxHeight);
        //LogUtil.e("ratio=" + ratio);
        is.close();
        newOpts.inSampleSize = (int) ratio;
        newOpts.inJustDecodeBounds = true;
        is = new FileInputStream(file);
        BitmapFactory.decodeStream(is, null, newOpts);
        is.close();
        int loopcnt = 0;
        while (newOpts.outWidth > maxWidth) {
            is = new FileInputStream(file);
            newOpts.inSampleSize += 1;
            BitmapFactory.decodeStream(is, null, newOpts);
            is.close();
            /*if (loopcnt > 3) {
	            break;
            }
            loopcnt++;*/
        }
        //LogUtil.e("width---" + newOpts.outWidth);
        newOpts.inJustDecodeBounds = false;
        return newOpts;
    }

    /**
     * 计算起始压缩比例
     * 先根据实际图片大小估算出最接近目标大小的压缩比例
     * 减少循环压缩的次数
     *
     * @return
     */
    public static double getOptRatio(InputStream is, int maxWidth, int maxHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);
        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;
        int destWidth = 0;
        int destHeight = 0;
        // 缩放的比例
        double ratio = 1.0;
        double ratio_w = 0.0;
        double ratio_h = 0.0;
        // 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
        if (srcWidth <= maxWidth && srcHeight <= maxHeight) {
            return ratio;   //小于屏幕尺寸时，不压缩
        }
        if (srcWidth > srcHeight) {
            ratio_w = srcWidth / maxWidth;
            ratio_h = srcHeight / maxHeight;
        } else {
            ratio_w = srcHeight / maxWidth;
            ratio_h = srcWidth / maxHeight;
        }
        if (ratio_w > ratio_h) {
            ratio = ratio_w;
        } else {
            ratio = ratio_h;
        }
        return ratio;
    }

    private static String CompressPngFile(Context c, String oldPath, InputStream is, BitmapFactory.Options newOpts, String filePath) throws Exception {
        Bitmap destBm = BitmapFactory.decodeStream(is, null, newOpts);

        destBm = rotateBitmapToExifOrientation(c, oldPath, destBm);

        return CompressPngFile(destBm, newOpts, filePath);
    }
    public static Bitmap rotateBitmapToExifOrientation(final Context context, final String filePath, final Bitmap bitmap) {
        final int orientation = ImageUtil.getExifOrientationFromJpeg(context, filePath);
        final int degrees;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degrees = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degrees = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degrees = 270;
                break;
            default:
                // Do nothing
                return bitmap;
        }

        final Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


    }


    public static int getExifOrientationFromJpeg(final Context context, final String filePath) {
        try {
            final ExifInterface exif = new ExifInterface(filePath);
            return Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));

        } catch (final Exception e) {

            return 0;
        }
    }

    private static String CompressPngFile(Bitmap destBm, BitmapFactory.Options newOpts, String filePath) throws Exception {
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        if (destBm == null) {
            return null;
        } else {
            File destFile = createNewFile(filePath);

            // 创建文件输出流
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                // 存储

                // 存储
                byte[] temp = new byte[1024];
                //添加时间水印
                Bitmap newbm = addTimeFlag(destBm);

                int rate = 90;
                newbm.compress(Bitmap.CompressFormat.PNG, rate, os);

                os.close();
                return filePath;
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
    }
    /**
     * 添加时间水印
     *
     * @param
     */
    private static Bitmap addTimeFlag(Bitmap src) {
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas mCanvas = new Canvas(newBitmap);
        // 往位图中开始画入src原始图片
        mCanvas.drawBitmap(src, 0, 0, null);
        //添加文字
        Paint textPaint = new Paint();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextSize(16);

        mCanvas.drawText(time, (float) (w * 1) / 14, (float) (h * 17) / 18, textPaint);
        mCanvas.save();
        mCanvas.restore();
        return newBitmap;
    }
}
