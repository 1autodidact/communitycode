package com.wenmrong.community1.community;

import com.wenmrong.community1.community.model.User;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
public class testThread {
    @Test
    public void testThread() throws InterruptedException {
        ReentrantLock a = new ReentrantLock();
        ReentrantLock b = new ReentrantLock();
        AtomicInteger index = new AtomicInteger();
        Condition aCondition = a.newCondition();
        Condition bCondition = a.newCondition();
        Condition cCondition = a.newCondition();
        for (int i = 0; i < 3; i++) {
            try {


                Thread threadA = new Thread(() -> {
                  a.lock();
                    while (index.get() % 3 == 0) {
                        System.out.println("A");
                    }
                    index.getAndIncrement();
                    bCondition.signal();
                });

                Thread threadB = new Thread(() -> {
                    a.lock();
                    while (index.get() % 3 == 1) {
                        System.out.println("B");
                    }
                    index.getAndIncrement();

                });


                Thread threadC = new Thread(() -> {
                    a.lock();
                    while (index.get() % 3 == 2) {
                        System.out.println("C");
                    }
                    index.getAndIncrement();


                });

                threadA.start();
                threadB.start();
//                threadC.start();

                Thread.currentThread().join();
                System.out.println("end");
            } finally {
                a.unlock();
            }

        }
    }

    @Test
    public void atomicReferenceTest() {

        AtomicInteger counter = new AtomicInteger();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                int expectValue = counter.get();
                boolean comparativeResult = counter.compareAndSet(expectValue, expectValue + 1);
                System.out.println(Thread.currentThread().getName() + "比较的结果 " + comparativeResult);
            });
            thread.start();
        }

    }


    @Test
    public void reference() throws InterruptedException {
        User user = new User();
        user.setName("ccc");
        AtomicReference<User> userAtomicReference = new AtomicReference<>(user);
        AtomicInteger counter = new AtomicInteger();
        User updateUserA = new User();
        updateUserA.setName("updateA");

        User updateUserB = new User();
        updateUserB.setName("updateB");
        for (int i = 0; i < 100; i++) {
            Thread threadB = new Thread(() -> {
                user.setName("BBBB");
                for (int i1 = 0; i1 < 2; i1++) {
                    System.out.println(Thread.currentThread().getName() + "counter" + counter.get());
                   int c = counter.get();
                   while(!counter.compareAndSet(c, counter.get() + 1)){

                   }
                    boolean compareResultB = counter.compareAndSet(counter.get(), counter.get() + 1);
                    System.out.println("比较结果" + compareResultB);
                }


            });
            threadB.start();

        }

       Thread.currentThread().join();

    }
}
