package amatur;

public class Cancelamento implements Runnable {
    private final Onibus onibus;
    private final long delayMillis;

    public Cancelamento(Onibus onibus, long delayMillis) {
        this.onibus = onibus;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        onibus.cancelarAssento();
    }
}

