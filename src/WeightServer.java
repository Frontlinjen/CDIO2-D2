import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WeightServer {
	String inline;
	String indtDisp= "";
	ConnectionHandler connection;
	static final int defaultPort = 8000;
	static boolean rm20flag = false;
	private void connect(int port) throws IOException
	{
		connection = new ConnectionHandler(port);
		System.out.println("Venter paa connection p� port " + port );
		System.out.println("Indtast eventuel portnummer som 1. argument");
		System.out.println("paa kommando linien for andet portnr");
		
	}
	public void start()
	{
		start(defaultPort);
	}
	public void start(int port)
	{
		connect(port);
		printmenu();
		try{
			String[] tokens = null;
			while ((tokens = connection.getCommand())!=null){ //her ventes p� input
				switch(tokens[0])
				{
				case "RM": //Skriv i display, afvent indtastning
				{
					break;
				}
				case "D": //Udskriv til display
				{
					indtDisp=(inline.substring(2, inline.length()));
					printmenu();
					outstream.writeBytes("DB"+"\r\n");
					break;
				}
				case "DW": //Reset Display
				{
					indtDisp=null;
					break;
				}
				case "T": //Tarer v�gten
				{
					outstream.writeBytes("T S " + (tara) + " kg "+"\r\n");
					tara=brutto;
					printmenu();
					break;
				}
				case "S": //Afvej
				{
					printmenu();
					outstream.writeBytes("S S " + (brutto-tara)+ " kg "  +"\r\n");
					break;
				}
				case "B": //Set bruttov�gt
				{
					String temp= inline.substring(2,inline.length());
					brutto = Double.parseDouble(temp);
					printmenu();
					outstream.writeBytes("DB"+"\r\n");
					break;
				}
				case "Q": //Aflsut program
				{
					disconnect();
				}
			}
				System.out.println(String.join(",", tokens));
			}
		}
		catch (Exception e){
			System.out.println("Exception: "+e.getMessage());
		}
	}
	private void disconnect()
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
		int port = defaultPort;
		try
		{
			if(args.length>0)
			{
				port = Integer.parseUnsignedInt(args[0]);
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("Specified port was not a number. Resetting to default.");
		}
		finally
		{
			WeightServer ws = new WeightServer();
			ws.start(port);
		}
		
	}
	public void printmenu(){
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
		System.out.println("Denne v�gt simulator lytter p� ordrene           ");
		System.out.println("S, T, D 'TEST', DW, RM20 8 .... , B og Q         ");
		System.out.println("p� kommunikationsporten.                         ");
		System.out.println("******")                             ;
		System.out.println("Tast T for tara (svarende til knaptryk paa vegt)") ;
		System.out.println("Tast B for ny brutto (svarende til at belastningen paa vegt �ndres)");
		System.out.println("Tast Q for at afslutte program program");
		System.out.println("Indtast (T/B/Q for knaptryk / brutto �ndring / quit)");
		System.out.print  ("Tast her: ");
	}
}