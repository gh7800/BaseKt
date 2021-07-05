package cn.shineiot.base.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;
/**
 * @author GF63
 */
public class ThreadUtil {
    /**
     * 创建线程池
     * @param corePoolSize 核心线程数
     * @return
     */
    public static ExecutorService getExecutorService(int corePoolSize) {
        return new ThreadPoolExecutor(corePoolSize,8,0L,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(1024),new MyThreadFactory());
    }
}
