

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class ConnectionHandler {
	private Socket sock;
	private BufferedReader instream;
	private DataOutputStream outstream;
	private ServerSocket listener;
	private boolean fresh = true;
	String[] getCommand()
	{
		String command = null;
		try {
			System.out.println("Venter p\u00E5 kommando...");
			if(!(command = instream.readLine().toUpperCase()).isEmpty())
			{
				System.out.println(command);
				if(fresh)
				{
					//Removes initial connection bogus
//					if(command.length()>21)
//						command = command.substring(21, command.length());
					fresh = false;
					System.out.println(command);
				}
				CommandParser tokenizer = new CommandParser(command);
				return tokenizer.getTokens();
			}
		} catch (IOException e) {
			System.out.println("Fejl: Et problem opstod under modtagelse af kommando:" + e.getMessage());
		}
		catch(NullPointerException e)
		{
			System.out.println("Forbindelse tabt for tidligt");
		}
		return null;
	}
	public ConnectionHandler(int port) throws IOException
	{
			listener = new ServerSocket(port);
			sock = listener.accept();
			instream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			outstream = new DataOutputStream(sock.getOutputStream());
			SendMessage("WeightSimulator 1.2");
			System.out.println("Skipping...");
	}
	public void SendMessage(String s)
	{
		try {
			outstream.writeBytes(s+"\r\n");
		} catch (IOException e) {
			System.out.println("Fejl: Et problem opstod da der blev printet til display");
			e.printStackTrace();
		}
	}
	public void SendError(String s)
	{
		SendMessage("ES " + s);
	}
	public void close() throws IOException
	{
		instream.close();
		outstream.close();
	}
}
