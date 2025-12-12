/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
