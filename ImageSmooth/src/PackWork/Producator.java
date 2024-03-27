package PackWork;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Producator extends Thread {

    private Buffer buffer;
    private String filePath;

    
    // constructor cu parametri
    public Producator(Buffer buff, String filePath) {
        buffer = buff;
        this.filePath = filePath;
    }

    // metoda run la pornirea thread-ului
    @SuppressWarnings("resource")
	public void run() {
    	// inceputul productiei
        long startTime = System.currentTimeMillis();
    	
    	// se creeaza un obiect File din filePath
        File file = new File(this.filePath);
        
        // lungimea fisierului
        long length = file.length();
        
        // FileInputStream pentru citirea imaginii
        FileInputStream aFile;

        // se initializeaza FileInputStream cu fisierul dat
        try {
            aFile = new FileInputStream(this.filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        // se obtine canalul fisierului
        FileChannel inChannel = aFile.getChannel();
        
        // se aloca un ByteBuffer pentru citirea unui sfert de imagine
        ByteBuffer buf = ByteBuffer.allocate((int) length / 4);

        
        // bucla pentru citirea imaginii
        for (int i = 0; i < 4; i++) {
            try {
            	
            	// se seteaza pozitia in fisier
                inChannel.position(i * (int) length / 4);

                // se ajusteaza dimensiunea lui ByteBuffer pentru ultima iteratie
                if (i == 3) {
                    buf = ByteBuffer.allocate((int) length / 4 + (int) length % 4);
                }

                // se citeste imaginea in ByteBuffer 
                @SuppressWarnings("unused")
				int bytesRead = inChannel.read(buf);
                
                // se converteste imaginea intr-un vector de bytes
                byte[] arr = buf.array();
                
                // se pune imaginea in buffer
                buffer.put(arr);
                
                // se curata buffer-ul pentru urmatoarea iteratie
                buf.clear();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            // sfarsitul productiei
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            synchronized ("afisare") {
                try {
                    System.out.println("Producatorul a pus :\t" + (i + 1) + "/4 din imagine in " + duration + " milisecunde");
                    sleep((int) (1000));
                } catch (InterruptedException e) {
                }
            }
        }
    }
}