package cn.shineiot.base.utils;

import android.os.CountDownTimer;

/**
 * @author GF63
 * 定时器
 */
public class CountDownTimerUtil extends CountDownTimer {
	/**
	 * @param millisInFuture  总数
	 * @param countDownInterval 间隔
	 */
	private CallBack callBack;

	public static long curMillis =0;   //初始时间戳
	public static boolean FLAG_FIRST_IN =true;

	public CountDownTimerUtil(long millisInFuture, long countDownInterval, CallBack callBack) {
		super(millisInFuture, countDownInterval);
		this.callBack = callBack;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		callBack.countDown(millisUntilFinished);
	}

	public void timerStart(boolean onClick){

		if(onClick) {
			CountDownTimerUtil.curMillis= System.currentTimeMillis();
		}
		CountDownTimerUtil.FLAG_FIRST_IN=false;
		this.start();
	}


	@Override
	public void onFinish() {
		this.cancel();
		callBack.countDownFinish();
	}

	/**
	 * 取消
	 */
	public void cancelCountDown(){
		this.cancel();
	}


	public interface CallBack{
		void countDown(long millisUntilFinished);
		void countDownFinish();
	}

}
