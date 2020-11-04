package task1;

import java.util.concurrent.Semaphore;

/**
 * Дан класс:
 * <p>
 * public class Foo {
 * public void first() { print("first"); }
 * public void second() { print("second"); }
 * public void third() { print("third"); }
 * }
 * <p>
 * Один и тот же экземпляр данного класса будет вызван 3мя разными потоками. Поток А - будет вызывать метод first(). Поток B - second(). Поток С - third().
 * Необходимо реализовать механизм и изменить программу таким образом, что методы класса Foo будут вызваны в правильном порядке.
 * <p>
 * <p>
 * Пример:
 * Вывод: "firstsecondthird"
 * Мы не знаем, в каком порядке будут вызваны методы, но должны гарантировать порядок.
 **/

public class Main {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Foo foo = new Foo();
            Thread thread1 = new Thread(() -> {
                try {
                    foo.second();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            Thread thread2 = new Thread(() -> {
                try {
                    foo.first();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            Thread thread3 = new Thread(() -> {
                try {
                    foo.third();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread1.start();
            thread2.start();
            thread3.start();

            thread1.join();
            thread2.join();
            thread3.join();
            System.out.println();
        }
    }

    static class Foo {
        Semaphore semS = new Semaphore(0);
        Semaphore semT = new Semaphore(0);

        public void first() throws InterruptedException {

            System.out.print("first");
            semS.release();
        }

        public void second() throws InterruptedException {
            semS.acquire();
            System.out.print("second");
            semT.release();
        }

        public void third() throws InterruptedException {
            semT.acquire();
            System.out.print("third");
        }
    }
}

