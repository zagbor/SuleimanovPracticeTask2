package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Задание 2
 * <p>
 * Необходимо написать программу, которая выводит строковое представление числа от 1 до N.
 * Если число делится без остатка на 3, представление "fizz".
 * Если число делится без остатка на 5, представление "buzz".
 * Если число делится без остатка и на 3, и на 5, представление "fizzbuzz".
 * Пример, для  n = 15,вывод будет: 1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz.
 * Имеется код
 * class FizzBuzz {
 *      public FizzBuzz(int n) { ... }
 *      public void fizz(printFizz) { ... }
 *      public void buzz(printBuzz) { ... }
 *      public void fizzbuzz(printFizzBuzz) { ... }
 *      public void number(printNumber) { ... }
 * }
 * Необходимо реализовать многопоточную версию данного класса для 4-х потоков. Один и тот же экземпляр будет передаваться в разные потоки.
 * Поток A вызывает fizz() для проверки деления без остатка на 3 и выводит fizz.
 * Поток B вызывает buzz() для проверки деления без остатка на 5 и выводит buzz.
 * Поток C вызывает fizzbuzz() для проверки деления без остатка на 3 и на 5 и выводит fizzbuzz.
 * Поток D вызывает number() который выводит только числа.
 */
public class FizzBuzzTask {
    volatile int n = 15;

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            FizzBuzzTask fizzBuzzTask = new FizzBuzzTask();
            FizzBuzz fizzBuzz = new FizzBuzz(fizzBuzzTask.n);
            for (int i : fizzBuzz.list) {
                Thread thread1 = new Thread(() -> {
                    try {
                        fizzBuzz.buzz(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                Thread thread2 = new Thread(() -> {
                    try {
                        fizzBuzz.fizz(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                Thread thread3 = new Thread(() -> {
                    try {
                        fizzBuzz.fizzBuzz(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                Thread thread4 = new Thread(() -> {
                    try {
                        fizzBuzz.number(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread1.start();
                thread2.start();
                thread3.start();
                thread4.start();
                thread1.join();
                thread2.join();
                thread3.join();
                thread4.join();

            }
            System.out.println();
        }
    }
}

class FizzBuzz {
    Semaphore fizzBuzzSemaphore = new Semaphore(1);
    Semaphore semFizz = new Semaphore(0);
    Semaphore semBuzz = new Semaphore(0);
    Semaphore semNumber = new Semaphore(0);
    List<Integer> list = new ArrayList<>();
    AtomicBoolean isFizz = new AtomicBoolean(false);
    AtomicBoolean isBuzz = new AtomicBoolean(false);
    AtomicBoolean isFizzBuzz = new AtomicBoolean(false);

    public FizzBuzz(int n) {
        int i = 1;
        while (i <= n) {
            list.add(i);
            i++;
        }
    }

    public void fizz(int n) throws InterruptedException {
        semFizz.acquire();
        if (n % 3 == 0 && !isFizzBuzz.get()) {
            System.out.print("fizz,");
            isFizz.set(true);
        }
        semNumber.release();
    }


    public void buzz(int n) throws InterruptedException {
        semBuzz.acquire();
        if (n % 5 == 0 && !isFizzBuzz.get()) {
            System.out.print("buzz,");
            isBuzz.set(true);
        }
        semNumber.release();
    }


    public void fizzBuzz(int n) throws InterruptedException {
        fizzBuzzSemaphore.acquire();
        if (n % 3 == 0 && n % 5 == 0) {
            System.out.print("fizzbuzz,");
            isFizzBuzz.set(true);
        }
        semBuzz.release();
        semFizz.release();
    }


    public void number(int n) throws InterruptedException {
        semNumber.acquire(2);
        if (!isFizz.get() && !isBuzz.get() && !isFizzBuzz.get()) {
            System.out.print(n + ",");
        }

        if (list.get(list.size() - 1) != n) {
            isFizz.set(false);
            isBuzz.set(false);
            isFizzBuzz.set(false);
            fizzBuzzSemaphore.release();
        }
    }
}

