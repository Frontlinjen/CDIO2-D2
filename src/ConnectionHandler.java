

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
				StringTokenizer tokenizer = new StringTokenizer(command);
				int index = 0;
				String[] tokens = new String[tokenizer.countTokens()];
				while(tokenizer.hasMoreTokens())
				{
					tokens[index++] = tokenizer.nextToken();
				}
				return tokens;
			}
		} catch (IOException e) {
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
			e.printStackTrace();
		}
	}
	public void close() throws IOException
	{
		instream.close();
		outstream.close();
	}
}
