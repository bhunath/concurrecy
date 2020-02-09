package multithreading.performance;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JavaLatency {

    private static String RESOURCE = "./resources/many-flowers.jpg";
    private static String OUTPUT = "./out/many-flowers-out.jpg";

    private static int getGreen(int rgb){
        return ((rgb & 0x0000FF00) >> 8);
    }

    private static int getBlue(int rgb){
        return rgb & 0x000000FF;
    }

    private static int getRed(int rgb){
        return (rgb & 0x00FF0000) >> 16;
    }

    private static int createColourFromRGBValue(int red, int green,int blue){
        int rgb = 0;
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    private static boolean isShadeOfGrey(int red,int green, int blue){
        return Math.abs(red-green) < 30 && Math.abs(green - blue) < 30 && Math.abs(blue - red) < 30;
    }

    public static void main(String[] args) throws IOException {
        final BufferedImage flowerImage = ImageIO.read(new File(RESOURCE));
        final BufferedImage resultImage = new BufferedImage(flowerImage.getWidth(), flowerImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        final long start = System.currentTimeMillis();
        //recolorImageSingleThreaded(flowerImage,resultImage);
        recolorImageMultiThread(flowerImage,resultImage,4);
        final long end = System.currentTimeMillis();

        System.out.println("Duration is  : " + (end -start));

        final File outputResult = new File(OUTPUT);
        ImageIO.write(resultImage,"jpg",outputResult);
    }

    private static void recolorImageMultiThread(BufferedImage originalImage , BufferedImage resultImage , int numberOfThrads){

        int width = originalImage.getWidth();
        int height = originalImage.getHeight()/numberOfThrads;

        List<Thread> recolorThreads = new ArrayList<>();

        for (int i = 0; i < numberOfThrads ; i++) {
            int topCorner = height * i;

            Thread recolorThread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    recolorImage(originalImage,resultImage,0,topCorner,width,height);
                }
            };
            recolorThreads.add(recolorThread);
            recolorThread.start();
        }

        final Consumer<Thread> threadConsumerJoin = thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        recolorThreads.stream().forEach(threadConsumerJoin);

    }

    private static void recolorImageSingleThreaded(BufferedImage originalImage , BufferedImage resultImage){
        recolorImage(originalImage,resultImage,0,0,originalImage.getWidth(),originalImage.getHeight());
    }

    private static void recolorImage(BufferedImage originalImage , BufferedImage resultImage , int leftCorner , int topCorner,
                              int width , int height){

        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight() ; y++) {
                recolorPixel(originalImage,resultImage,x,y);
            }
        }
    }

    private static void recolorPixel(BufferedImage originalImage, BufferedImage recoloredImage , int x , int y){
        int rgbValue = originalImage.getRGB(x,y);

        int red = getRed(rgbValue);
        int green = getGreen(rgbValue);
        int blue = getBlue(rgbValue);

        int newRed;
        int newGreen;
        int newBlue;

        if(isShadeOfGrey(red,green,blue)){
            newRed = Math.min(255,red + 10);
            newGreen = Math.max(0,green  - 80);
            newBlue = Math.max(0,blue  - 20);
        }else{
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createColourFromRGBValue(newRed,newGreen,newBlue);
        setRGB(recoloredImage,x,y,newRGB);

    }

    private static void setRGB(BufferedImage bufferedImage , int x, int y , int rgb){
        bufferedImage.getRaster().setDataElements(x,y, bufferedImage.getColorModel().getDataElements(rgb,null));
    }


}
