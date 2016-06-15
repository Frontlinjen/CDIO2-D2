import java.io.IOException;
import java.util.Scanner;

public class WeightServer {
	private String indtDisp= "";
	private WeightData data = new WeightData();
	private ConnectionHandler connection;
	static final int defaultPort = 8000;
	static boolean rm20flag = false;
	private class CommandFormatException extends Exception
	{
		private static final long serialVersionUID = 1L;
		CommandFormatException(String s)
		{
			super(s);
		}
	}
	private void connect(int port) throws IOException
	{
		connection = new ConnectionHandler(port);
		System.out.println("Venter p\u00E5 connection p\u00E5 port " + port );
		System.out.println("Indtast eventuel portnummer som 1. argument");
		System.out.println("P\u00E5 kommando linien for andet portnr");
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
			System.out.println("Fejl: Et problem opstod under forbindelse til port " + port + ":\t" + e1.getMessage());
			return;
		}
		printmenu();
		String[] tokens = null;
		Scanner sc = new Scanner(System.in);
		while ((tokens = connection.getCommand())!=null){ //her ventes p� input
			try{
				switch(tokens[0])
				{
				case "RM20": //Skriv i display, afvent indtastning
				{
					switch(tokens[1]){
					case "8": 
						String message = tokens[2];
						String unit = tokens[4];
						if(message.length() >= 30){
							System.out.println(message.substring(0, 29));
						}
						else System.out.println(message);
						connection.SendMessage("RM20 B");
						String input = null;
						while(sc.hasNextLine())
						{
							input = sc.nextLine();
							CommandParser command = new CommandParser(input);
							if(command.nextToken().equals("B"))
							{
								data.setBrutto(Double.parseDouble(command.nextToken()));
								System.out.println("Successfully updated weight");
							}
							else
							{
								break;
							}							
						}
						
						connection.SendMessage("RM20 A " + input.substring(0, input.length()));
						break;
					}
					break;
				}
				
				case "D": //Udskriv til display
				{
					indtDisp = "";
					for(int i = 1; i < tokens.length; i++){
						indtDisp += " " + tokens[i];
					}
					if(indtDisp.length() > 8)
						indtDisp = indtDisp.substring(0, 8);
					printmenu();
					connection.SendMessage("D A");
					break;
				}
				case "DW": //Reset Display
				{
					indtDisp=data.getNetto()+ "kg";
					connection.SendMessage("DW A");
					printmenu();
					break;
				}
				case "T": //Tarer weight
				{
					data.setTara(data.getBrutto());
					connection.SendMessage("T S " + (data.getTara()) + " kg ");
					printmenu();
					break;
				}
				case "S": //Deweight
				{
					printmenu();
					connection.SendMessage("S S " + (data.getNetto())+ " kg ");
					break;
				}
				case "B": //Set brutto weight
				{
					if (tokens.length < 1){
						connection.SendError("Fejl, tokens b\u00F8r v\u00E6re mindst 1");
					}
					else{
						try{
							data.setBrutto(Double.parseDouble(tokens[1]));
						} catch(Exception e){
							throw new CommandFormatException("Ingen brutto angivet");
						}
						printmenu();
						connection.SendMessage("DB");
					}
					break;
				}
				case "Q": //Afslutter programmet
				{
					disconnect();
					break;
				}

				case "P111": //Udskriver til sekundaert display
				{
					if(tokens.length<2)
					{
						throw new CommandFormatException("Ikke argumenter givet!");
					}
					indtDisp = "";
					for(int i = 1; i < tokens.length; i++){
						indtDisp += " " + tokens[i];
					}
					if(indtDisp.length() > 31){
						indtDisp = indtDisp.substring(0, 31);
					}
					printmenu();
					connection.SendMessage("P111 A");
					break;
				}
				default:
				{
					connection.SendError("Ukendt kommando");
				}
				}
			}
			catch (Exception e){
				connection.SendError(e.getMessage());
				e.printStackTrace();
			}
		}
		sc.close();
	}
	private void disconnect()
	{
		System.out.println("");
		System.out.println("Program stoppet Q modtaget p\u00E5 com   port");
		try {
			System.in.close();
			System.out.close();
			connection.close();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Programmet fejlede i at lukke ned ordenligt");;
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
			System.out.println("Specificerede port var ikke et nummer. Reset til default port.");
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
		System.out.println("Sekund\u00F8rt instruktionsdisplay: " +  indtDisp    );
		System.out.println("*************************************************");
		System.out.println("                                                 ");
		System.out.println("                                                 ");
		System.out.println("Brutto: " + (data.getBrutto())+ " kg"             );
		System.out.println("Tara: " + (data.getTara())+ " kg"              	);
		System.out.println("                                                 ");
		System.out.println("Denne v\u00E6gt simulator lytter p\u00E5 ordrene           ");
		System.out.println("S, T, D 'TEST', DW, RM20 8 .... , B og Q         ");
		System.out.println("p\u00E5 kommunikationsporten.                         ");
		System.out.println("******")                             ;
		System.out.println("Tast T for tara (svarende til knaptryk p\u00E5 v\u00E6gt)") ;
		System.out.println("Tast B for ny brutto (svarende til at belastningen p\u00E5 v\u00E6gt \u00E6ndres)");
		System.out.println("Tast Q for at afslutte program");
		System.out.println("Indtast (T/B/Q for knaptryk / brutto \u00E6ndring / quit)");
		System.out.print  ("Tast her: ");
	}
}