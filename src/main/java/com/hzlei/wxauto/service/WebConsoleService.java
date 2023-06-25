package com.hzlei.wxauto.service;

import com.hzlei.wxauto.pool.ThreadPoolManager;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class WebConsoleService {
    final static String shutdown = "shutdown";
    public static ConcurrentHashMap<String, Thread> consoleThreadMap = new ConcurrentHashMap<>();  // 创建hashmap，用于存储线程

    public void webConsole(String serial,
                           String cmd,
                           int msgid) {
        try {
            // 如果不是终止指令则通过线程池创建线程
            if (!cmd.equalsIgnoreCase("shutdown")) {
                System.out.println("==========创建指令========");
                ThreadPoolManager.getInstance().addExecuteTask(new EachT(cmd, serial));
            } else {
                // 如果是终止指令，则执行interrupt()终止指定的某条线程，这里具体线程通过serial指定
                System.out.println("==========终止指令========");
                consoleThreadMap.get(serial).interrupt();
            }
        } catch (Exception e) {
        }
    }

    private class EachT implements Runnable {

        private String cmd;
        private String serial;

        public EachT(String cmd,
                     String serial) {
            this.cmd = cmd;
            this.serial = serial;
        }

        @Override
        public void run() {
            try {
                consoleThreadMap.put(serial, Thread.currentThread());
                execute(cmd);
            } catch (Exception e) {

            } finally {
                // 执行完成后，将线程从hashmap中移除
                consoleThreadMap.remove(serial);
            }
        }

        public void execute(String cmd) throws InterruptedException {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());

                Thread.sleep(2000);
            }
        }

    }
}