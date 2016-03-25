

public class WeightData {

	private double brutto=0;
	private double tara=0;
	
	public double getBrutto(){
		return brutto;
	}
	
	public void setBrutto(double brutto){
		this.brutto = brutto;
	}
	
	public double getTara(){
		return tara;
	}
	
	public void setTara(double tara){
		this.tara = tara;
	}
	
	public double getNetto(){
		return brutto - tara;
	}
	
	
}
