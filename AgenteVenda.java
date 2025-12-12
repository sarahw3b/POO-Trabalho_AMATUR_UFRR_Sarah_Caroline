/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package amatur;

import java.util.concurrent.Semaphore;

public class AgenteVenda implements Runnable {
    private final Onibus onibus;
    private final String nome;
    private final String modo; // "part1","part2","part3","part4"
    private final Semaphore sem; // pode ser null se não usado

    public AgenteVenda(Onibus onibus, String nome, String modo) {
        this(onibus, nome, modo, null);
    }

    public AgenteVenda(Onibus onibus, String nome, String modo, Semaphore sem) {
        this.onibus = onibus;
        this.nome = nome;
        this.modo = modo;
        this.sem = sem;
    }

    @Override
    public void run() {
        switch (modo) {
            case "part1":
                onibus.reservarSemSincronizacao(nome);
                break;
            case "part2":
                onibus.reservarComSynchronizedBlock(nome);
                break;
            case "part3":
                onibus.reservarComWaitNotify(nome);
                break;
            case "part4":
                if (sem == null) {
                    throw new IllegalStateException("Semaforo nao fornecido para part4");
                }
                System.out.printf("[%s] tentando entrar no servidor (aguardando permissao)...%n", nome);
                onibus.reservarComSemaphore(nome, sem);
                break;
            default:
                throw new IllegalArgumentException("Modo desconhecido: " + modo);
        }
    }
}
