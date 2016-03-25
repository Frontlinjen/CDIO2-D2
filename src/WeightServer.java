import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class WeightServer {
	String indtDisp= "";
	String indtSecDisp = "";
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
				case "RM20": //Skriv i display, afvent indtastning
				{
					switch(tokens[1]){
						case "8": 
							String command = "";
							for(int i = 2; i < tokens.length; i++){
								command += " " + tokens[i];
							}
							StringTokenizer tokenizer = new StringTokenizer(command, "\"");
							String message = tokenizer.nextToken();
							//Skip unnecessary input
							tokenizer.nextToken();
							String length = tokenizer.nextToken();
							length = length.substring(1);
							int max = Integer.parseInt(length);
							System.out.println(message.substring(0, 29));
							connection.SendMessage("RM20 B \r\n");
							Scanner sc = new Scanner(System.in);
							String input = sc.nextLine();
							connection.SendMessage("RM20 A " + input.substring(0, max-1) + "\r\n");
							sc.close();
							
							
							
							break;
					}
					break;
				}
				case "D": //Udskriv til display
				{
					for(int i = 1; i < tokens.length; i++){
						indtDisp += " " + tokens[i];
					}
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
				
				case "P111": //Udskriv til sekundært display
				{
					for(int i = 1; i < tokens.length; i++){
						indtSecDisp += " " + tokens[i];
					}
					
					indtSecDisp = indtSecDisp.substring(0, 29);
					printmenu();
					connection.SendMessage("D A"+"\r\n");
					break;
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
		System.out.println("Sekundært instruktionsdisplay: " +  indtSecDisp    );
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