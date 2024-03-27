package PackWork;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public final class Printer {

    // functie de citire de la tastatura
    public static String read() {
        System.out.println("Input image path: ");
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    // functie de printare a unei imagini
    public static void printImage(BufferedImage... images) {
        for (BufferedImage image : images) {
            System.out.println("Output image path: ");
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
            String imagePath = scanner.nextLine();
            try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                ImageIO.write(image, "bmp", fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
