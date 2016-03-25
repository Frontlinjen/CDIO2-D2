import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WeightServer {
	String indtDisp= "";
	WeightData data = new WeightData();
	ConnectionHandler connection;
	static final int defaultPort = 8000;
	static boolean rm20flag = false;
	private void connect(int port) throws IOException
	{
		connection = new ConnectionHandler(port);
		System.out.println("Venter paa connection på port " + port );
		System.out.println("Indtast eventuel portnummer som 1. argument");
		System.out.println("paa kommando linien for andet portnr");
		
	}
	public void start()
	{
		start(defaultPort);
	}
	public void start(int port)
	{
		try {
			connect(port);
		} catch (IOException e1) {
			System.out.println("Error: A problem occurred while waiting for a connection on port: " + port);
			e1.printStackTrace();
		}
		printmenu();
		try{
			String[] tokens = null;
			while ((tokens = connection.getCommand())!=null){ //her ventes på input
				switch(tokens[0])
				{
				case "RM": //Skriv i display, afvent indtastning
				{
					break;
				}
				case "D": //Udskriv til display
				{
					for(int i = 1; i < tokens.length; i++){
						indtDisp += " " + tokens[i];
					}
					printmenu();
					connection.SendMessage("DB"+"\r\n");
					break;
				}
				case "DW": //Reset Display
				{
					indtDisp=null;
					break;
				}
				case "T": //Tarer vægten
				{
					data.setTara(data.getBrutto());
					connection.SendMessage("T S " + (data.getTara()) + " kg "+"\r\n");
					printmenu();
					break;
				}
				case "S": //Afvej
				{
					printmenu();
					connection.SendMessage("S S " + (data.getNetto())+ " kg "  +"\r\n");
					break;
				}
				case "B": //Set bruttovægt
				{
					data.setBrutto(Double.parseDouble(tokens[2]));
					printmenu();
					connection.SendMessage("DB"+"\r\n");
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
		for (int i=0;i<2;i++)
		System.out.println("                                                 ");
		System.out.println("*************************************************");
		System.out.println("Netto: " + (data.getNetto())+ " kg"                   );
		System.out.println("Instruktionsdisplay: " +  indtDisp    );
		System.out.println("*************************************************");
		System.out.println("                                                 ");
		System.out.println("                                                 ");
		System.out.println("Brutto: " + (data.getBrutto())+ " kg"             );
		System.out.println("Tara: " + (data.getTara())+ " kg"              	);
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