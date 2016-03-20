import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class VsCon {
	static ServerSocket listener;
	static double brutto=0;
	static double tara=0;
	static String inline;
	static String indtDisp= "";
	static int portdst = 8000;
	static Socket sock;
	static BufferedReader instream;
	static DataOutputStream outstream;
	static boolean rm20flag = false;

	public static void main(String[] args) throws IOException{
		listener = new ServerSocket(portdst);
		System.out.println("Venter paa connection på port " + portdst );
		System.out.println("Indtast eventuel portnummer som 1. argument");
		System.out.println("paa kommando linien for andet portnr");
		sock = listener.accept();
		instream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		outstream = new DataOutputStream(sock.getOutputStream());
		printmenu();
		try{
			while (!(inline = instream.readLine().toUpperCase()).isEmpty()){ //her ventes på input
				if (inline.startsWith("RM")){                        
					// ikke implimenteret

				}
				else if (inline.startsWith("D")){
					if (inline.equals("DW"))
						indtDisp="";
					else
						indtDisp=(inline.substring(2, inline.length()));//her skal anførselstegn udm.
					printmenu();
					outstream.writeBytes("DB"+"\r\n");
				}
				else if (inline.startsWith("T")){
					outstream.writeBytes("T S " + (tara) + " kg "+"\r\n");        //HVOR MANGE SPACE?
					tara=brutto;
					printmenu();
				}
				else if (inline.startsWith("S")){
					printmenu();
					outstream.writeBytes("S S " + (brutto-tara)+ " kg "  +"\r\n");//HVOR MANGE SPACE?
				}
				else if (inline.startsWith("B")){ //denne ordre findes ikke på en fysisk vægt
					String temp= inline.substring(2,inline.length());
					brutto = Double.parseDouble(temp);
					printmenu();
					outstream.writeBytes("DB"+"\r\n");
				}
				else if ((inline.startsWith("Q"))){
					System.out.println("");
					System.out.println("Program stoppet Q modtaget paa com   port");
					System.in.close();
					System.out.close();
					instream.close();
					outstream.close();
					System.exit(0);
				}


				else { 
					printmenu();
					outstream.writeBytes("ES"+"\r\n");
				}
			}
		}
		catch (Exception e){
			System.out.println("Exception: "+e.getMessage());
		}
	}
	public static void printmenu(){
		for (int i=0;i<2;i++)
			System.out.println("                                                 ");
		System.out.println("*************************************************");
		System.out.println("Netto: " + (brutto-tara)+ " kg"                   );
		System.out.println("Instruktionsdisplay: " +  indtDisp    );
		System.out.println("*************************************************");
		System.out.println("                                                 ");
		System.out.println("                                                 ");
		System.out.println("Debug info:                                      ");
		System.out.println("Hooked up to " + sock.getInetAddress()            );
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