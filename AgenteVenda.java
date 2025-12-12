package amatur;

import java.util.concurrent.Semaphore;

public class AgenteVenda implements Runnable {
    private final Onibus onibus;
    private final String nome;
    private final String modo; 
    private final Semaphore sem; 
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


