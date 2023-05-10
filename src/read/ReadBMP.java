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
    private int bitCount = 0;
    private String bfType = null;
    private int adress = 0;
    private int[] pixels = null;
    private int[] pixelsYCbCr = null;
    private int[] restoredPixels = null;
    private int[] pixelsDecim2X = null;
    private int[] pixelsDecim4X = null;
    private int[] restoredPixelsFromDecim  = null;
     

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

    	char first =(char) massiv[0];
    	char second = (char) massiv[1];
    	System.out.println(first +" "+ second);
    	bfType = new String(first + " " + second);
    	int byte1 = massiv[2];
    	System.out.println(Integer.toBinaryString((int)massiv[2]));
    	System.out.println(Integer.toBinaryString((int)massiv[3] << 8));
    	System.out.println(Integer.toBinaryString((int)massiv[4] << 16));
    	System.out.println(Integer.toBinaryString((int)massiv[5]));
    	byte1 = byte1 ^ ((int)massiv[3] << 8); 
    	System.out.println(Integer.toBinaryString(byte1));
    	byte1 = byte1 ^ ((int)massiv[4] << 16); 
    	System.out.println(Integer.toBinaryString(byte1));
    	byte1 = byte1 ^ ((int)massiv[5] << 24);
    	System.out.println(Integer.toBinaryString(byte1));
    	System.out.println(byte1);
    	adress = massiv[10];
    	adress = adress ^ ((int)massiv[11] << 8);
    	adress = adress ^ ((int)massiv[12] << 16);
    	adress = adress ^ ((int)massiv[13] << 24);
    	width = ((int)massiv[18]);
    	width = width ^ ((int)massiv[19] << 8);
    	width = width ^ ((int)massiv[20] << 16);
    	width = width ^ ((int)massiv[21] << 24);
    	height = massiv[22];
    	height = height ^ ((int)massiv[23] << 8);
    	height = height ^ ((int)massiv[24] << 16);
    	height = height ^ ((int)massiv[25] << 24);
    	bitCount = (int)massiv[28];
    	
    	System.out.println(width + " " + height + " " + bitCount);

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
//    	imgPixels = new byte[entryImage.length - 55];
//    	for (int i = 56, j = 0; i < entryImage.length - 1 && j < imgPixels.length - 1; i ++, j++) {
//    		imgPixels[j] = entryImage[i];
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
    	for(int i = this.height- 1; i > 0 ; i -- ) {
    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
    				pixels[(i * width) + j] = getPixel(i , j  );
    				pixels[(i * width) + k] = getPixel(i , k) ;
    		}
    	}
    	
//    	for(int i = 0; i < this.height- 1 ; i ++ ) {
//    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
//    				pixels[(i * width) + j] = getPixel(i , k  );
//    				pixels[(i * width) + k] = getPixel(i , j ) ;
//    		}
//    	}
    	
    	pixelsYCbCr = new int[width * height];
    	
    	for(int i = 0; i < this.height- 1 ; i ++ ) {
    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
    				pixelsYCbCr[(i * width) + k] = getPixelInCrCbY(i , j  );
    				pixelsYCbCr[(i * width) + j] = getPixelInCrCbY(i , k ) ;
    		}
    	}
    	
    	turningOutFromYCbRToRGB();
    	createImG();
    	createImGYCb();
    	createRestoredImg();
    	for(int i = 0; i < this.height- 1 ; i ++ ) {
    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
    				pixelsYCbCr[(i * width) + k] = getPixelInCrCbY(i , k  );
    				pixelsYCbCr[(i * width) + j] = getPixelInCrCbY(i , j ) ;
    		}
    	}
    	int[] pixelsYCbCrLoc = new int[width * height];
    	for(int i = 0; i < this.height- 1 ; i ++ ) {
    		for(int j = this.width - 1, k = 0; j > (this.width/2) - 1 && k < this.width/2 ; j--, k++) {
    				pixelsYCbCrLoc[(i * width) + k] = getPixelInCrCbY(i , k  );
    				pixelsYCbCrLoc[(i * width) + j] = getPixelInCrCbY(i , j ) ;
    		}
    	}
    	pixelsDecim2X = decimation(pixelsYCbCr, 2);
    	int[] pixelsDecimLoc = decimation(pixelsYCbCrLoc, 2);
    	//createDecim(pixelsDecimLoc);
    	int[] pixelsRecoveredAfter = recover(pixelsDecimLoc, 2);
    	//createDecim(pixelsRecoveredAfter);
    	pixelsRecoveredAfter = returningFromYCbCr(pixelsRecoveredAfter);
    	pixelsDecim2X = pixelsRecoveredAfter;
    	//createDecim(pixelsRecoveredAfter);
    	//createDecim(pixelsRecoveredAfter);
    	//System.out.println("Red comp " + getRAfterDecim(2, 10, 26));
    	pixelsDecimLoc = decimation(pixelsYCbCrLoc, 4);
    	//createDecim(pixelsDecimLoc);
    	pixelsRecoveredAfter = recover(pixelsDecimLoc, 4);
    	//createDecim(pixelsRecoveredAfter);
    	pixelsRecoveredAfter = returningFromYCbCr(pixelsRecoveredAfter);
    	pixelsDecim4X = pixelsRecoveredAfter;
    	createDecim(pixelsRecoveredAfter);
    	
    	System.out.println("Success2");

    
    }
    
    public int getRAfterDecim(int size, int x, int y) {
    	if(size == 2){
    		return (pixelsDecim2X[(x * width) + y] >> 16) & 0xFF;
    	}
    	else {
    		return 0;
    	}
    }
    
    public int getGAfterDecim(int size, int x, int y) {
    	if(size == 2){
    		return (pixelsDecim2X[(x * width) + y] >> 8) & 0xFF;
    	}
    	else {
    		return 0;
    	}
    }
    
    public int getBAfterDecim(int size, int x, int y) {
    	if(size == 2){
    		return (pixelsDecim2X[(x * width) + y]) & 0xFF;
    	}
    	else {
    		return (pixelsDecim4X[(x * width) + y]) & 0xFF;
    	}
    }
    
    private int transferFromYCbCr(int element) {
    	int Y = 0;
    	int Cb = 0;
    	int Cr = 0;
    	Y = (element >> 16) & 0xFF;
    	Cb = (element >> 8) & 0xFF;
    	Cr = (element) & 0xFF;
    	int R = (int)(Y + 1.402 * (Cr - 128)) & 0xFF;
    	int G = (int)(Y - 0.714 * (Cr - 128) - 0.334 * (Cb - 128)) & 0xFF;
    	int B = (int)(Y + 1.772 * (Cb - 128)) & 0xFF;
    	//System.out.println(R + " " + G + " " + B);
    	//getRcomp(x, y) & 0xFF) << 16 | (getGcomp(x, y) & 0xFF) << 8 | (getBcomp(x, y) & 0xFF)
    	element = R << 16 | G << 8 | B;
    	return element;
    }
    
    private int[] returningFromYCbCr(int[] input) {
    	int[] returning = new int[input.length];
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			returning[(i * width) + j] = transferFromYCbCr(input[(i * width) + j]);
    		}
    	}
    	return returning;
    }
    
    private void turningOutFromYCbRToRGB() {
    	
    	restoredPixels = new int[width * height];
    	
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			restoredPixels[(i * width) + j] = getPixelFromYCbCr(i , j);
    		}
    	}
    	
    	
    }
    
//    public void changePlaces() {
//    	pixels = restoredPixelsFromDecim;
//    }
    
    public void turningOutFromYCbRTDecimToRGB() {
    	
    	restoredPixelsFromDecim = new int[width * height];
    	
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			restoredPixelsFromDecim[(i * width) + j] = getPixelFromYCbCr(i , j);
    		}
    	}
    	
    	
    }
    
    public int[] decimation (int[] input, int size){
        int [] result = new int[(width * height) / size];
        for (int i = 0; i < (this.height); i += size) {
           
            for (int j = 0; j < this.width; j += size) {
                result[(i/size) * width + (j/size)] = input[i * width + j];
            }
        }
        return result;
    }
    
   
    
    public int getPixelFromYCbCr(int x, int y) {
    	return (getRcomp(x, y) & 0xFF) << 16 | (getGcomp(x, y) & 0xFF) << 8 | (getBcomp(x, y) & 0xFF);
    }
    
    public int[] recover(int[] input, int size){
        int[] recoverYCbCr = new int[(width * height)];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
            	//System.out.println((i/size) + " " + (j/size) + " " + input[(i/size) * width + (j/size)]);
                recoverYCbCr[(i * width) + j] = input[((i/size) * width) + (j/size)]; 
            }
        }
        return recoverYCbCr;
    }
    
    public int getGcomp(int x, int y) {
    	return (int)(getYComp(x, y) - 0.714 * (getCrComp(x, y) - 128) - 0.334 * (getCbComp(x, y) - 128));
    }
    
    public int getRcomp(int x, int y) {
    	return (int)(getYComp(x, y) + 1.402 * (getCrComp(x, y) - 128));
    }
    
    public int getBcomp(int x, int y) {
    	return (int)(getYComp(x, y) + 1.772 * (getCbComp(x, y) - 128));
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
    
    
    public void createRestoredImg() throws IOException {
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    	 
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			bufferedImage.setRGB(j, i, restoredPixels[(i * width) + j]);
    		}
    	}
    	
    	ImageIO.write(bufferedImage, "BMP", new File("res/RestoredIMG.bmp"));
    
    }
    
    public void createImGYCb() throws IOException {
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
 
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			bufferedImage.setRGB(j, i, pixelsYCbCr[(i * width) + j]);
    		}
    	}
    	

    	ImageIO.write(bufferedImage, "BMP", new File("res/Y.bmp"));
    
    }
    
    public void createDecim(int[] temp) throws IOException {
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
 
    	for(int i = 0; i < height; i++) {
    		for(int j = 0; j < width; j++) {
    			bufferedImage.setRGB(j, i, temp[(i * width) + j]);
    		}
    	}
    	

    	ImageIO.write(bufferedImage, "BMP", new File("res/decimAfter.bmp"));
    
    }
    
    public void createAfterDecim() throws IOException {
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
 
    	for(int i = 0; i < height/2; i++) {
    		for(int j = 0; j < width/2; j++) {
    			bufferedImage.setRGB(j, i, pixels[(i * width) + j]);
    		}
    	}
    	

    	ImageIO.write(bufferedImage, "BMP", new File("res/afterDecim.bmp"));
    
    }
  
}

