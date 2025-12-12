/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package amatur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n==============================");
        System.out.println("   PARTE I - RACE CONDITION   ");
        System.out.println("==============================\n");
        executarParte1();

        System.out.println("\n==============================");
        System.out.println("  PARTE II - SYNCHRONIZED     ");
        System.out.println("==============================\n");
        executarParte2();

        System.out.println("\n==============================");
        System.out.println(" PARTE III - WAIT / NOTIFY    ");
        System.out.println("==============================\n");
        executarParte3();

        System.out.println("\n==============================");
        System.out.println(" PARTE IV - SEMAFOROS         ");
        System.out.println("==============================\n");
        executarParte4();
    }

    private static void executarParte1() throws InterruptedException {
        Onibus onibus = new Onibus(5);
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            Thread t = new Thread(new AgenteVenda(onibus, "Agente-" + i, "part1"));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) t.join();

        System.out.printf("Fim Parte I - assentos finais: %d%n", onibus.getAssentosDisponiveis());
    }

    private static void executarParte2() throws InterruptedException {
        Onibus onibus = new Onibus(5);
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            Thread t = new Thread(new AgenteVenda(onibus, "Agente-" + i, "part2"));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) t.join();

        System.out.printf("Fim Parte II - assentos finais: %d%n", onibus.getAssentosDisponiveis());
    }

    private static void executarParte3() throws InterruptedException {
    Onibus onibus = new Onibus(2);
    List<Thread> threads = new ArrayList<>();

    // 5 agentes
    for (int i = 1; i <= 5; i++) {
        Thread t = new Thread(new AgenteVenda(onibus, "Agente-" + i, "part3"));
        threads.add(t);
        t.start();
    }

    // 3 cancelamentos (um por segundo)
    for (int i = 0; i < 3; i++) {
        new Thread(new Cancelamento(onibus, 1000 * (i + 1))).start();
    }

    for (Thread t : threads) t.join();

    System.out.printf("Fim Parte III - assentos finais: %d%n", onibus.getAssentosDisponiveis());
}

    private static void executarParte4() throws InterruptedException {
        Onibus onibus = new Onibus(5);
        Semaphore sem = new Semaphore(3);
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            Thread t = new Thread(new AgenteVenda(onibus, "Agente-" + i, "part4", sem));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) t.join();

        System.out.printf("Fim Parte IV - assentos finais: %d%n", onibus.getAssentosDisponiveis());
    }
}
