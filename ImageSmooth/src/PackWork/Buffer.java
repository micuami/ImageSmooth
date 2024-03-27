package PackWork;

public class Buffer {
    private byte[] buff;
    private boolean available = false;

    // semafoare de sincronizare pentru operatia get()
    public synchronized byte[] get() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
        notifyAll();
        return buff;
    }
    
    // semafoare de sincronizare pentru operatia put()
    public synchronized void put(byte[] buff) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.buff = buff;
        available = true;
        notifyAll();
    }
}
