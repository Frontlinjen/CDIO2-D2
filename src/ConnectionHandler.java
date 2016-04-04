

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
			System.out.println("Listening for command...");
			if(!(command = instream.readLine().toUpperCase()).isEmpty())
			{
				if(fresh)
				{
					//Removes initial connection bogus
					command = command.substring(21, command.length());
					fresh = false;
				}
				CommandParser tokenizer = new CommandParser(command);
				return tokenizer.getTokens();
			}
		} catch (IOException e) {
			System.out.println("Error: A problem occurred when recieving command");
			e.printStackTrace();
		}
		return null;
	}
	public ConnectionHandler(int port)
	{
		try {
			listener = new ServerSocket(port);
			sock = listener.accept();
			instream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			outstream = new DataOutputStream(sock.getOutputStream());
			
			System.out.println("Skipping...");
		} catch (IOException e) {
			System.out.println("Error: A problem occurred when connecting to server");
			e.printStackTrace();
		}
	}
	public void SendMessage(String s)
	{
		try {
			outstream.writeBytes(s+"\r\n");
		} catch (IOException e) {
			System.out.println("Error: A problem occurred when printing to display");
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
