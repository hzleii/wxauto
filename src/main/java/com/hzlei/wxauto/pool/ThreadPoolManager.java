package com.hzlei.wxauto.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public final class ThreadPoolManager {
    private static ThreadPoolManager sThreadPoolManager = new ThreadPoolManager();

    // 线程池维护线程的最少数量
    private static final int SIZE_CORE_POOL = 50;

    // 线程池维护线程的最大数量
    private static final int SIZE_MAX_POOL = 100;

    /*
     * 线程池单例创建方法
     */
    public static ThreadPoolManager getInstance() {
        return sThreadPoolManager;
    }

    /**
     * 常见的几种线程技术
     * Java通过Executors提供四种线程池，分别为：
     * newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     * newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。 newSingleThreadExecutor
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     * <p>
     * 1、public static ExecutorService newFixedThreadPool(int nThreads) {
     * return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()); }
     * <p>
     * 2、 public static ExecutorService newSingleThreadExecutor() {
     * return new FinalizableDelegatedExecutorService (new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())); }
     * <p>
     * 3、public static ExecutorService newCachedThreadPool() {
     * return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()); }
     */

    /**
     * 线程池
     *
     * @param corePoolSize - 池中所保存的线程数，包括空闲线程。
     * @param maximumPoolSize - 池中允许的最大线程数。
     * @param keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
     * @param unit - keepAliveTime 参数的时间单位。
     * @param workQueue - 执行前用于保持任务的队列。此队列仅由保持 execute 方法提交的 Runnable 任务。
     * @param handler - 由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序。
     */
    // 实质就是newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
    private final ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(SIZE_CORE_POOL,
        SIZE_MAX_POOL,
        0L,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(),
        new CustomizableThreadFactory("HZLEI-THREAD-"),
        new ThreadPoolExecutor.AbortPolicy());

    /*
     * 将构造方法访问修饰符设为私有，禁止任意实例化。
     */
    private ThreadPoolManager() {
        prepare();
    }

    /*
     * 将线程池初始化，核心线程数量
     */
    private void prepare() {
        if (mThreadPool.isShutdown() && !mThreadPool.prestartCoreThread()) {
            @SuppressWarnings("unused") int startThread = mThreadPool.prestartAllCoreThreads();
        }
    }

    /*
     * 向线程池中添加任务方法
     */
    public void addExecuteTask(Runnable task) {
        if (task != null) {
            mThreadPool.execute(task);
        }
    }

    /*
     * 判断是否是最后一个任务
     */
    protected boolean isTaskEnd() {
        if (mThreadPool.getActiveCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 获取缓存大小
     */
    public int getQueue() {
        return mThreadPool.getQueue().size();
    }

    /*
     * 获取线程池中的线程数目
     */
    public int getPoolSize() {
        return mThreadPool.getPoolSize();
    }

    /*
     * 获取已完成的任务数
     */
    public long getCompletedTaskCount() {
        return mThreadPool.getCompletedTaskCount();
    }

    /*
     * 关闭线程池，不在接受新的任务，会把已接受的任务执行玩
     */
    public void shutdown() {
        mThreadPool.shutdownNow();
    }
}