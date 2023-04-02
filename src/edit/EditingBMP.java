package edit;

import read.*;


public class EditingBMP {
	
	private ReadBMP reader;
	
	private double MATHEXPECTATIONRED = 0;
	
	private double MATHEXPECTATIONGREEN = 0;
	
	private double MATHEXPECTATIONBLUE = 0;
	
	private double MEDIANSSQUAREMISSRED = 0;
	
	private double MEDIANSSQUAREMISSGREEN = 0;
	
	private double MEDIANSSQUAREMISSBLUE = 0;
	
	private double MATHEXPECTATIONY = 0;
	
	private double MATHEXPECTATIONCb = 0;
	
	private double MATHEXPECTATIONCr = 0;
	
	private double MEDIANSSQUAREMISSY = 0;
	
	private double MEDIANSSQUAREMISSCb = 0;
	
	private double MEDIANSSQUAREMISSCr = 0;
	
	
	public EditingBMP(ReadBMP reader) {
		this.reader = reader;
		
		calculateMathExpectations();
		
		medianSquareMiss();
	}
	
	public  void coefCorr() {
		double RG = 0;
		double RB = 0;
		double GB = 0;
		double YCr = 0;
		double YCb = 0;
		double CbCr = 0;
		for(int i = 0; i < reader.getHeight(); i ++) {
			for(int j = 0; j < reader.getWidth(); j++ ) {
				RG += (reader.getRedColour(i, j) - MATHEXPECTATIONRED) * (reader.getGreenColour(i, j) - MATHEXPECTATIONGREEN);
				RB += (reader.getRedColour(i, j) - MATHEXPECTATIONRED) * (reader.getBlueColour(i, j) - MATHEXPECTATIONBLUE);
				GB += (reader.getGreenColour(i, j) - MATHEXPECTATIONGREEN) * (reader.getBlueColour(i, j) - MATHEXPECTATIONBLUE);
				YCr += (reader.getYComp(i, j) - MATHEXPECTATIONY) * (reader.getCrComp(i, j) - MATHEXPECTATIONCr);
				YCb += (reader.getYComp(i, j) - MATHEXPECTATIONY) * (reader.getCbComp(i, j) - MATHEXPECTATIONCb);
				CbCr += (reader.getCbComp(i, j) - MATHEXPECTATIONCb) * (reader.getCrComp(i, j) - MATHEXPECTATIONCr);
			}
		}
		RG = (RG /(reader.getHeight() * reader.getWidth())) / (MEDIANSSQUAREMISSRED * MEDIANSSQUAREMISSGREEN);
		RB = (RB /(reader.getHeight() * reader.getWidth())) / (MEDIANSSQUAREMISSRED * MEDIANSSQUAREMISSBLUE);
		GB = (GB /(reader.getHeight() * reader.getWidth())) / (MEDIANSSQUAREMISSGREEN * MEDIANSSQUAREMISSBLUE);
		YCr = (YCr /(reader.getHeight() * reader.getWidth())) / (MEDIANSSQUAREMISSY * MEDIANSSQUAREMISSCr);
		YCb = (YCb /(reader.getHeight() * reader.getWidth())) / (MEDIANSSQUAREMISSY * MEDIANSSQUAREMISSCb);
		CbCr = (CbCr /(reader.getHeight() * reader.getWidth())) / (MEDIANSSQUAREMISSCb * MEDIANSSQUAREMISSCr);
		
		System.out.println("RG " + RG + " RB " + RB + " GB " + GB );
		
		System.out.println("YCr " + YCr + " YCb " + YCb + " CrCb " + CbCr );
	}
	
	private void calculateMathExpectations() {
		for(int i = 0; i < reader.getHeight(); i++) {
			for(int j = 0; j < reader.getWidth(); j++) {
				
				MATHEXPECTATIONRED += reader.getRedColour(i, j);
				
				MATHEXPECTATIONGREEN += reader.getGreenColour(i, j);
				
				MATHEXPECTATIONBLUE += reader.getBlueColour(i, j);
				
				MATHEXPECTATIONY += reader.getYComp(i, j);
				
				MATHEXPECTATIONCb += reader.getCbComp(i, j);
				
				MATHEXPECTATIONCr += reader.getCrComp(i, j);
				
			}
		}
		
		MATHEXPECTATIONRED = MATHEXPECTATIONRED / (reader.getHeight() * reader.getWidth());
		
		MATHEXPECTATIONGREEN = MATHEXPECTATIONGREEN / (reader.getHeight() * reader.getWidth());
		
		MATHEXPECTATIONBLUE = MATHEXPECTATIONBLUE / (reader.getHeight() * reader.getWidth());
		
		MATHEXPECTATIONY = MATHEXPECTATIONY / (reader.getHeight() * reader.getWidth());
		
		MATHEXPECTATIONCb = MATHEXPECTATIONCb / (reader.getHeight() * reader.getWidth());
		
		MATHEXPECTATIONCr = MATHEXPECTATIONCr / (reader.getHeight() * reader.getWidth());
	}
	
	private void medianSquareMiss() {
		for(int i = 0; i < reader.getHeight(); i ++) {
			for(int j = 0; j < reader.getWidth(); j ++) {
				
				 MEDIANSSQUAREMISSRED += Math.pow(((double)reader.getRedColour(i, j) - MATHEXPECTATIONRED), 2);
					
				 MEDIANSSQUAREMISSGREEN += Math.pow(((double)reader.getGreenColour(i, j) - MATHEXPECTATIONGREEN), 2);
					
				 MEDIANSSQUAREMISSBLUE += Math.pow(((double)reader.getBlueColour(i, j) - MATHEXPECTATIONBLUE), 2);
				 
				 MEDIANSSQUAREMISSY += Math.pow(((double)reader.getYComp(i, j) - MATHEXPECTATIONY), 2);
					
				 MEDIANSSQUAREMISSCb += Math.pow(((double)reader.getCbComp(i, j) - MATHEXPECTATIONCb), 2);
					
				 MEDIANSSQUAREMISSCr += Math.pow(((double)reader.getCrComp(i, j) - MATHEXPECTATIONCr), 2);
			}
		}
		
		 MEDIANSSQUAREMISSRED = Math.pow(MEDIANSSQUAREMISSRED /(reader.getHeight() * reader.getWidth() - 1), 0.5);
		
		 MEDIANSSQUAREMISSGREEN =  Math.pow(MEDIANSSQUAREMISSGREEN /(reader.getHeight() * reader.getWidth() - 1), 0.5) ;
			
		 MEDIANSSQUAREMISSBLUE =  Math.pow(MEDIANSSQUAREMISSBLUE /(reader.getHeight() * reader.getWidth() - 1), 0.5);
		 
		 MEDIANSSQUAREMISSY = Math.pow(MEDIANSSQUAREMISSY /(reader.getHeight() * reader.getWidth() - 1), 0.5);
			
		 MEDIANSSQUAREMISSCr =  Math.pow(MEDIANSSQUAREMISSCr /(reader.getHeight() * reader.getWidth() - 1), 0.5) ;
			
		 MEDIANSSQUAREMISSCb =  Math.pow(MEDIANSSQUAREMISSCb /(reader.getHeight() * reader.getWidth() - 1), 0.5);
		
	}
	
	
	
	public double getMathExpectRed() {
		return MATHEXPECTATIONRED;
	}
	
	public double getMathExpectGreen() {
		return MATHEXPECTATIONGREEN;
	}
	
	public double getMathExpectBlue() {
		return MATHEXPECTATIONBLUE;
	}
	
	public double getSquareMissRed() {
		return MEDIANSSQUAREMISSRED;
	}
	
	public double getSquareMissGreen() {
		return MEDIANSSQUAREMISSGREEN;
	}
	
	public double getSquareMissBlue() {
		return MEDIANSSQUAREMISSBLUE;
	}
	
}
