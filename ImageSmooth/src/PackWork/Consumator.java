package PackWork;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Consumator implements Runnable, ImageProcessor {

    private Buffer buffer;

    public Consumator(Buffer buff) {
        buffer = buff;
    }

    @Override
    public void run() {
    	
    	// se initializeaza variabile pentru stocarea imaginii
        byte[] value;
        byte[] destination = new byte[0];

        // bucla pentru a consuma imaginea din buffer
        for (int i = 0; i < 4; i++) {
        	
        	// se obtine din buffer un sfert din poza
            value = buffer.get();
            
            // se concateneaza la datele deja existente
            byte[] atThisStep;
            atThisStep = new byte[destination.length + value.length];
            System.arraycopy(destination, 0, atThisStep, 0, destination.length);
            System.arraycopy(value, 0, atThisStep, destination.length, value.length);

            // destinatia primeste noile date
            destination = atThisStep;

            
            // afisarea sincronizata a mesajelor
            synchronized ("afisare") {
                try {
                    System.out.println("Consumatorul a primit :\t" + (i + 1) + "/4 din imagine");
                    
                    // timp de delay
                    Thread.sleep((int) (1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // se creeaza un BufferedImage din datele obtinute
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(destination));
        } catch (IOException e) {
        	// se trateaza exceptia afisand calea
            e.printStackTrace();
        }

        // se proceseaza imaginea folosind interfata ImageProcessor
        processImage(img);
    }

    @Override
    public void processImage(BufferedImage img) {
    	
    	// inceputul procesarii imaginii
        long startTime = System.currentTimeMillis();
    	
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        
        // se aplica masca de convolutie Gaussian smooth
        for (int i = 1; i < width-1 ; i++) {
            for (int j = 1; j < height-1 ; j++) {
               
            	// se extrag din fiecare vecin al pixelului culorile
                int rgb1 = img.getRGB(i - 1, j - 1);
                int r1 = (rgb1 >> 16) & 0xFF;
                int g1 = (rgb1 >> 8) & 0xFF;
                int b1 = rgb1 & 0xFF;
                int rgb2 = img.getRGB(i - 1, j);
                int r2 = (rgb2 >> 16) & 0xFF;
                int g2 = (rgb2 >> 8) & 0xFF;
                int b2 = rgb2 & 0xFF;
                int rgb3 = img.getRGB(i - 1, j + 1);
                int r3 = (rgb3 >> 16) & 0xFF;
                int g3 = (rgb3 >> 8) & 0xFF;
                int b3 = rgb3 & 0xFF;
                int rgb4 = img.getRGB(i, j - 1);
                int r4 = (rgb4 >> 16) & 0xFF;
                int g4 = (rgb4 >> 8) & 0xFF;
                int b4 = rgb4 & 0xFF;
                int rgb5 = img.getRGB(i, j);
                int r5 = (rgb5 >> 16) & 0xFF;
                int g5 = (rgb5 >> 8) & 0xFF;
                int b5 = rgb5 & 0xFF;
                int rgb6 = img.getRGB(i, j + 1);
                int r6 = (rgb6 >> 16) & 0xFF;
                int g6 = (rgb6 >> 8) & 0xFF;
                int b6 = rgb6 & 0xFF;
                int rgb7 = img.getRGB(i + 1, j - 1);
                int r7 = (rgb7 >> 16) & 0xFF;
                int g7 = (rgb7 >> 8) & 0xFF;
                int b7 = rgb7 & 0xFF;
                int rgb8 = img.getRGB(i + 1, j);
                int r8 = (rgb8 >> 16) & 0xFF;
                int g8 = (rgb8 >> 8) & 0xFF;
                int b8 = rgb8 & 0xFF;
                int rgb9 = img.getRGB(i, j + 1);
                int r9 = (rgb9 >> 16) & 0xFF;
                int g9 = (rgb9 >> 8) & 0xFF;
                int b9 = rgb9 & 0xFF;
                
                // se calculeaza culorile noului pixel
                int newRed = (r1 + r2*2 + r3 + r4*2 + r5*4 + r6*2 + r7 + r8*2 + r9)/16;
                if(newRed>255) newRed=255;
                if(newRed<0) newRed=0;
                  
                int newGreen = (g1 + g2*2 + g3 + g4*2 + g5*4 + g6*2 + g7 + g8*2 + g9)/16;
                if(newGreen>255) newGreen=255;
                if(newGreen<0) newGreen=0;
                
                int newBlue = (b1 + b2*2 + b3 + b4*2 + b5*4 + b6*2 + b7 + b8*2 + b9)/16;
                if(newBlue>255) newBlue=255;
                if(newBlue<0) newBlue=0; 
                
                // noul pixel
                int newRGB = (newRed << 16) | (newGreen << 8) | newBlue;
                
                // se transmite noul pixel
                result.setRGB(i, j, newRGB);
            }
        }

        // sfarsitul procesarii imaginii
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // afisarea timpului pentru procesarea imaginii
        synchronized ("afisare") {
            System.out.println("Procesarea imaginii a durat " + duration + " milisecunde");
        }
        // se printeaza imaginea
        Printer.printImage(result);
    }
}
