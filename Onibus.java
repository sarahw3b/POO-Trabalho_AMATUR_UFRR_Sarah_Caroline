package amatur;

import java.util.concurrent.Semaphore;

public class Onibus {
    private int assentosDisponiveis;

    public Onibus(int assentos) {
        this.assentosDisponiveis = assentos;
    }

    public void reservarSemSincronizacao(String agente) {
        if (assentosDisponiveis > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assentosDisponiveis--;
            System.out.printf("[%s] reservou 1 (restam=%d)%n", agente, assentosDisponiveis);
        } else {
            System.out.printf("[%s] tentou reservar mas sem assentos (restam=%d)%n", agente, assentosDisponiveis);
        }
    }

    public void reservarComSynchronizedBlock(String agente) {
        try {
            Thread.sleep(50); // simula algum trabalho antes da checagem
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronized (this) {
            if (assentosDisponiveis > 0) {
                try {
                    Thread.sleep(100); // simula latência dentro da seção crítica
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                assentosDisponiveis--;
                System.out.printf("[%s] reservou 1 (restam=%d)%n", agente, assentosDisponiveis);
            } else {
                System.out.printf("[%s] tentou reservar mas sem assentos (restam=%d)%n", agente, assentosDisponiveis);
            }
        }
    }

    public void reservarComWaitNotify(String agente) {
        synchronized (this) {
            while (assentosDisponiveis <= 0) {
                try {
                    System.out.printf("[%s] esperando assento (lotado)...%n", agente);
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.printf("[%s] interrompido enquanto aguardava%n", agente);
                    return;
                }
            }
            // ao sair do wait, existe pelo menos 1 assento
            try {
                Thread.sleep(100); // simula latência antes de decrementar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assentosDisponiveis--;
            System.out.printf("[%s] conseguiu reservar (restam=%d)%n", agente, assentosDisponiveis);
        }
    }

    // Parte III - cancelamento que incrementa e notifica
    public void cancelarAssento() {
        synchronized (this) {
            assentosDisponiveis++;
            System.out.printf("[Cancelamento] liberou 1 assento (restam=%d) e notificando threads%n", assentosDisponiveis);
            this.notifyAll(); // avisa as threads aguardando
        }
    }

    // Parte IV - usar semáforo: métodos úteis
    public void reservarComSemaphore(String agente, Semaphore sem) {
        try {
            sem.acquire();
            // dentro da "porta" controlada pelo semáforo, podemos verificar assentos
            synchronized (this) {
                if (assentosDisponiveis > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    assentosDisponiveis--;
                    System.out.printf("[%s] reservou 1 (restam=%d) - dentro do servidor (permits=%d)%n",
                            agente, assentosDisponiveis, sem.availablePermits());
                } else {
                    System.out.printf("[%s] tentou reservar mas sem assentos (restam=%d) - dentro do servidor (permits=%d)%n",
                            agente, assentosDisponiveis, sem.availablePermits());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            sem.release();
        }
    }

    public int getAssentosDisponiveis() {
        return assentosDisponiveis;
    }
}

