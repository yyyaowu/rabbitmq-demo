package com.yao.rabbitmq.utils;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.utils
 * @className: SleepUtils
 * @author: yao
 * @description: TODO 线程等待
 * @date: 2022/4/12 14:24
 * @version: 1.0
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
