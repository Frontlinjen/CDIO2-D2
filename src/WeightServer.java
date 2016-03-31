import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Scanner;

public class WeightServer {
	private String indtDisp= "";
	private String indtSecDisp = "";
	private WeightData data = new WeightData();
	private ConnectionHandler connection;
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
		try {
			connect(port);
			System.out.println("Successfully connected");
		} catch (IOException e1) {
			System.out.println("Error: A problem occurred while waiting for a connection on port: " + port);
			e1.printStackTrace();
		}
		printmenu();
		String[] tokens = null;
		while ((tokens = connection.getCommand())!=null){ //her ventes p� input
			try{
				switch(tokens[0])
				{
				case "RM20": //Skriv i display, afvent indtastning
				{
					switch(tokens[1]){
					case "8": 
						String message = tokens[2];
						String length = tokens[4];
						length = length.substring(1);
						int max = 0;
						try{
							Integer.parseInt(length);
						} 
						catch(Exception e){
							throw new Exception("Ingen brutto angivet");
						}
						if(message.length() >= 30){
							System.out.println(message.substring(0, 29));
						}
						else System.out.println(message);
						connection.SendMessage("RM20 B \r\n");
						Scanner sc = new Scanner(System.in);
						String input = sc.nextLine();
						connection.SendMessage("RM20 A " + input.substring(0, max-1) + "\r\n");
						sc.close();
						break;
					}
				}
				break;
				case "D": //Udskriv til display
				{
					indtDisp = "";
					for(int i = 1; i < tokens.length; i++){
						indtDisp += " " + tokens[i];
					}
					if(indtSecDisp.length() > 6)
						indtDisp = indtDisp.substring(0, 6);
					printmenu();
					connection.SendMessage("D A"+"\r\n");
					break;
				}
				case "DW": //Reset Display
				{
					indtDisp=data.getNetto()+ "kg";
					connection.SendMessage("DW A\r\n");
					break;
				}
				case "T": //Tarer v�gten
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
				case "B": //Set bruttov�gt
				{
					if (tokens.length < 1){
						System.out.println("Fejl, tokens b�r v�re mindst 1");
					}
					else{
						try{
							data.setBrutto(Double.parseDouble(tokens[1]));
						} catch(Exception e){
							throw new Exception("Ingen brutto angivet");
						}
						printmenu();
						connection.SendMessage("DB"+"\r\n");
					}
					break;
				}
				case "Q": //Afslut program
				{
					disconnect();
				}

				case "P111": //Udskriv til sekund�rt display
				{
					indtSecDisp = "";
					for(int i = 1; i < tokens.length; i++){
						indtSecDisp += " " + tokens[i];
					}
					if(indtSecDisp.length() > 29)
						indtSecDisp = indtSecDisp.substring(0, 29);
					printmenu();
					connection.SendMessage("D A"+"\r\n");
					break;
				}
				}
				System.out.println(String.join(",", tokens));
			}
			catch (Exception e){
				System.out.println("Exception: "+e.getMessage());
			}
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
		System.out.println("Sekund�rt instruktionsdisplay: " +  indtSecDisp    );
		System.out.println("*************************************************");
		System.out.println("                                                 ");
		System.out.println("                                                 ");
		System.out.println("Brutto: " + (data.getBrutto())+ " kg"             );
		System.out.println("Tara: " + (data.getTara())+ " kg"              	);
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