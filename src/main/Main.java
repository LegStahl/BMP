package main;

import java.io.*;

import edit.EditingBMP;
import read.ReadBMP;
import java.util.*;

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
			edit.calculatePSNR();
			edit.calculatePSNRAfterDec2X();
			edit.calculatePSNRAfterDec4X();
			ArrayList<Integer>  a = new ArrayList<>();
			a.add(2);
			
			//read.turningOutFromYCbRTDecimToRGB();
			//read.changePlaces();
			//edit.calculatePSNR();
			//read.createAfterDecim();
			//read.createImG();
		}catch(IOException e){
			
		}
	}
}
