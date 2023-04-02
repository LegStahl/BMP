package main;

import java.io.*;

import edit.EditingBMP;
import read.ReadBMP;

public class Main {
	public static void main(String[] argv) {
		try {
			ReadBMP read = new ReadBMP("res/kodim202.bmp", "read/new");
			EditingBMP edit = new EditingBMP(read);
			System.out.println(read.getRedColour(200, 100));
			System.out.println(read.getGreenColour(200, 100));
			System.out.println(read.getBlueColour(200, 100));
			System.out.println(edit.getMathExpectBlue() + " " + edit.getMathExpectGreen() + " " + edit.getMathExpectRed());
			System.out.println(edit.getSquareMissBlue() + " " + edit.getSquareMissGreen() + " " + edit.getSquareMissRed());
			edit.coefCorr();
			//read.createImG();
		}catch(IOException e){
			
		}
	}
}
