import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class VsCon {
	static double brutto=0;
	static double tara=0;
	static String inline;
	static String indtDisp= "";
	static int portdst = 8000;
	static ConnectionHandler connection;
	static boolean rm20flag = false;
	private static void connect(int port) throws IOException
	{
		connection = new ConnectionHandler(port);
		System.out.println("Venter paa connection på port " + port );
		System.out.println("Indtast eventuel portnummer som 1. argument");
		System.out.println("paa kommando linien for andet portnr");
		
	}
	private static void disconnect()
	{
		System.out.println("");
		System.out.println("Program stoppet Q modtaget paa com   port");
		try {
			System.in.close();
			System.out.close();
			connection.close();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws IOException{
		connect(portdst);
		printmenu();
		try{
			String[] tokens = null;
			while ((tokens = connection.getCommand())!=null){ //her ventes på input
				switch(tokens[0])
				{
				case "RM":
				{
					
				}
				case "D":
				{
					break;
				}
				case "DW":
				{
					break;
				}
				case "T":
				{
					break;
				}
				case "S":
				{
					break;
				}
				case "B":
				{
					break;
				}
				case "Q":
				{
					disconnect();
				}
				}
				System.out.println(String.join(",", tokens));
//				if (inline.startsWith("RM")){                        
//					// ikke implimenteret
//
//				}
//				else if (inline.startsWith("D")){
//					if (inline.equals("DW"))
//						indtDisp="";
//					else
//						indtDisp=(inline.substring(2, inline.length()));//her skal anførselstegn udm.
//					printmenu();
//					outstream.writeBytes("DB"+"\r\n");
//				}
//				else if (inline.startsWith("T")){
//					outstream.writeBytes("T S " + (tara) + " kg "+"\r\n");        //HVOR MANGE SPACE?
//					tara=brutto;
//					printmenu();
//				}
//				else if (inline.startsWith("S")){
//					printmenu();
//					outstream.writeBytes("S S " + (brutto-tara)+ " kg "  +"\r\n");//HVOR MANGE SPACE?
//				}
//				else if (inline.startsWith("B")){ //denne ordre findes ikke på en fysisk vægt
//					String temp= inline.substring(2,inline.length());
//					brutto = Double.parseDouble(temp);
//					printmenu();
//					outstream.writeBytes("DB"+"\r\n");
//				}
//				else if ((inline.startsWith("Q"))){
//					
//				}
//
//
//				else { 
//					printmenu();
//					outstream.writeBytes("ES"+"\r\n");
//				}
			}
		}
		catch (Exception e){
			System.out.println("Exception: "+e.getMessage());
		}
	}
	public static void printmenu(){
		if(true)
			return;
		for (int i=0;i<2;i++)
			System.out.println("                                                 ");
		System.out.println("*************************************************");
		System.out.println("Netto: " + (brutto-tara)+ " kg"                   );
		System.out.println("Instruktionsdisplay: " +  indtDisp    );
		System.out.println("*************************************************");
		System.out.println("                                                 ");
		System.out.println("                                                 ");
		System.out.println("Debug info:                                      ");
		System.out.println("Brutto: " + (brutto)+ " kg"                       );
		System.out.println("Streng modtaget: "+inline)                         ;
		System.out.println("                                                 ");
		System.out.println("Denne vægt simulator lytter på ordrene           ");
		System.out.println("S, T, D 'TEST', DW, RM20 8 .... , B og Q         ");
		System.out.println("på kommunikationsporten.                         ");
		System.out.println("******")                             ;
		System.out.println("Tast T for tara (svarende til knaptryk paa vegt)") ;
		System.out.println("Tast B for ny brutto (svarende til at belastningen paa vegt ændres)");
		System.out.println("Tast Q for at afslutte program program");
		System.out.println("Indtast (T/B/Q for knaptryk / brutto ændring / quit)");
		System.out.print  ("Tast her: ");
	}
}