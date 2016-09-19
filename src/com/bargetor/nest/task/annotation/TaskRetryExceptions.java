package com.bargetor.nest.task.annotation;

import java.lang.annotation.*;

/**
 * Created by Bargetor on 16/9/19.
 *
 * 当任务在执行过程中发生了一个已知异常,并希望发生此异常后,任务可以重试
 * 可以通过此标注表面需要重试的异常
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskRetryExceptions {
    Class<? extends Exception>[] value();
}
