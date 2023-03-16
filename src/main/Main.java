package main;

import java.io.*;

import read.ReadBMP;

public class Main {
	public static void main(String[] argv) {
		try {
			ReadBMP read = new ReadBMP("res/LightHouse.bmp", "read/new");
			//read.createImG();
		}catch(IOException e){
			
		}
	}
}
