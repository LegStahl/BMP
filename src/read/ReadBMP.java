package read;
//import com.

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;

import javax.imageio.stream.FileImageOutputStream;


public class ReadBMP {
    private String imgSave;
    private String imgLoad;
    private byte[] imgHeader;
    private byte[] imgPixels;
    private byte[] saveEncrypt;
    private byte[] entryImage;
    private int width = 0;
    private int height = 0;
    private String bfType = null;
    private int adress = 0;
    private int[] pixels = null;
    private int[] pixelsYCbCr = null;
    

    public ReadBMP(String imgLoad, String imgSave) throws IOException{
        this.imgLoad = imgLoad;
        this.imgSave = imgSave;
        getImgBytes();
    }
    
    public void getImgBytes() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(imgLoad));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "bmp", outputStream);
        System.out.println(bufferedImage);
        entryImage = outputStream.toByteArray();

        processHeader(entryImage);
      
        
    }
    
    private void processHeader(byte[] massiv) throws IOException { 

//    	char first =(char) massiv[0];
//    	char second = (char) massiv[1];
//    	System.out.println(first +" "+ second);
//    	bfType = new String(first + " " + second);
//    	int byte1 = massiv[2];
//    	System.out.println(Integer.toBinaryString((int)massiv[2]));
//    	System.out.println(Integer.toBinaryString((int)massiv[3] << 8));
//    	System.out.println(Integer.toBinaryString((int)massiv[4] << 16));
//    	System.out.println(Integer.toBinaryString((int)massiv[5]));
//    	byte1 = byte1 ^ ((int)massiv[3] << 8); 
//    	System.out.println(Integer.toBinaryString(byte1));
//    	byte1 = byte1 ^ ((int)massiv[4] << 16); 
//    	System.out.println(Integer.toBinaryString(byte1));
//    	byte1 = byte1 ^ ((int)massiv[5] << 24);
//    	System.out.println(Integer.toBinaryString(byte1));
//    	System.out.println(byte1);
//    	adress = massiv[10];
//    	adress = adress ^ ((int)massiv[11] << 8);
//    	adress = adress ^ ((int)massiv[12] << 16);
//    	adress = adress ^ ((int)massiv[13] << 24);
    	width = ((int)massiv[18]);
    	width = width ^ ((int)massiv[19] << 8);
    	width = width ^ ((int)massiv[20] << 16);
    	width = width ^ ((int)massiv[21] << 24);
    	height = massiv[22];
    	height = height ^ ((int)massiv[23] << 8);
    	height = height ^ ((int)massiv[24] << 16);
    	height = height ^ ((int)massiv[25] << 24);
    	
    	System.out.println(width + " " + height);

    	createMassivPixels();
    	
    
    }
    
    private void createMassivPixels() throws IOException {
    	pixels = new int[(width * height) ];
    	System.out.println(pixels.length);
    	
    	System.out.println(entryImage.length);
    	
//    	imgPixels = new byte[entryImage.length - 55];
//    	for(int i = 55; i < entryImage.length; i ++) {
//    		imgPixels[i - 55] = entryImage[i];
//    	}
    	imgPixels = new byte[entryImage.length - 55];
    	for (int i = entryImage.length - 1, j = 0; i > 55 && j < imgPixels.length - 1; i --, j++) {
    		imgPixels[j] = entryImage[i];
    	}
//    	for(int i = 0; i < this.height- 1 ; i ++ ) {
//    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
//    				pixels[(i * width) + j] = getPixel(i * 3, (k * 3) );
//    				pixels[(i * width) + k] = getPixel(i * 3, (j * 3) );
//    		}
//    	}
//    	
    	for(int i = 0; i < this.height- 1 ; i ++ ) {
    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
    				pixels[(i * width) + j] = getPixel(i , k  );
    				pixels[(i * width) + k] = getPixel(i , j ) ;
    		}
    	}
    	
    	pixelsYCbCr = new int[width * height];
    	
    	for(int i = 0; i < this.height- 1 ; i ++ ) {
    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
    				pixelsYCbCr[(i * width) + k] = getCr(i , k  );
    				pixelsYCbCr[(i * width) + j] = getCr(i , j ) ;
    		}
    	}
    	
 
    	createImG();
    	createImGYCb();

    	System.out.println("Success2");
    }
    
    public int getPixelInCrCbY(int x, int y) {
    	return (getYComp(x, y) & 0xFF) << 16 | (getCbComp(x, y) & 0xFF) << 8 | (getCrComp(x, y) & 0xFF);
    }
    
    public int getPixel(int i, int j) {
		
		return (imgPixels[(i * 3) * width + (j * 3)] & 0xFF ) << 16 | (imgPixels[(i * 3) * width + ((j * 3) + 1)] & 0xFF ) << 8 | (imgPixels[(i * 3) * width + (j * 3) + 2] & 0xFF );
	}
    
    public int getBlue(int i, int j) {
		
  		return (((imgPixels[i * width + (j)] & 0xFF) << 16 | (imgPixels[i * width + (j + 1)] & 0xFF) << 8 | (imgPixels[i * width + j + 2] & 0xFF)) & 0xFF);
  	}
    
    public int getGreen(int i, int j) {
		
  		return (((imgPixels[i * width + (j)] & 0xFF) << 16 | (imgPixels[i * width + (j + 1)] & 0xFF) << 8 | (imgPixels[i * width + j + 2] & 0xFF)) & 0xFF00);
  	}
    
    public int getRed(int i, int j) {
		
  		return (((imgPixels[i * width + (j)] & 0xFF) << 16 | (imgPixels[i * width + (j + 1)] & 0xFF) << 8 | (imgPixels[i * width + j + 2] & 0xFF)) & 0xFF0000);
  	}
    
    public int getY(int i, int j) {
    	return (getYComp(i, j)  << 16) | (getYComp(i, j)  << 8) | (getYComp(i, j)) & 0xFF ;
    }
    
    public int getCb(int i, int j) {
    	return (getCbComp(i, j)  << 16) | (getCbComp(i, j)  << 8) | (getCbComp(i, j)) ;
    }
    
    public int getCr(int i, int j) {
    	return (getCrComp(i, j) << 16) | (getCrComp(i, j) << 8) | (getCrComp(i, j)) ;
    }
    
    public int getRedColour(int x, int y) {
    	return (pixels[x * width + y] >> 16) & 0xFF;
    }
    
    public int getGreenColour(int x, int y) {
    	return (pixels[x * width + y] >> 8) & 0xFF;
    }
    
    public int getBlueColour(int x, int y) {
    	return (pixels[x * width + y]) & 0xFF;
    }
    
    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }
    
    public int getYComp(int x, int y) {
    	return ((int)((double)getRedColour(x, y) * 0.299 + 0.587 * (double)getGreenColour(x, y) + 0.114 * (double)getBlueColour(x, y)))& 0xFF ;
    }
    
    public int getCbComp(int x, int y) {
    	return ((int)(0.5643 * (getBlueColour(x, y) - getYComp(x, y)) + 128)) & 0xFF;
    }
    
    public int getCrComp(int x, int y) {
    	return ((int)(0.7132 * (getRedColour(x, y) - getYComp(x, y)) + 128)) & 0xFF;
    }

    
    public void createImG() throws IOException {
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
 
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			bufferedImage.setRGB(j, i, pixels[(i * width) + j]);
    		}
    	}
    	

    	ImageIO.write(bufferedImage, "BMP", new File("res/som2.bmp"));
    
    }
    
    public void createImGYCb() throws IOException {
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
 
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			bufferedImage.setRGB(j, i, pixelsYCbCr[(i * width) + j]);
    		}
    	}
    	

    	ImageIO.write(bufferedImage, "BMP", new File("res/Cr.bmp"));
    
    }
  
}

