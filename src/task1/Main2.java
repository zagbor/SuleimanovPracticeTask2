package task1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        Foo foo = new Foo(lock1, lock2);
        lock1.lock();
        lock2.lock();
        Thread thread1 = new Thread(() -> {
            try {
                foo.second();
                lock2.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                lock1.lock();
                foo.first();
                lock1.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                lock2.lock();
                foo.third();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        System.out.println();
    }


    static class Foo {
        private final Lock lock1;
        private final Lock lock2;

        Foo(Lock lock1, Lock lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
        }


        public void first() throws InterruptedException {
            System.out.print("first");

        }

        public void second() throws InterruptedException {
            System.out.print("second");
        }

        public void third() throws InterruptedException {
            System.out.print("third");
        }
    }
}
