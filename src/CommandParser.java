import java.util.ArrayList;
import java.util.List;

public class CommandParser {
	private String command;
	private int p = 0;
	public CommandParser(String s)
	{
		command = s;
	}
	//Todo: make it take double spaces into account
	public String nextToken()
	{
		if(p>=command.length())
		{
			return null;
		}
		if(command.length()>p && command.charAt(p)=='\"')
		{
			int i = command.indexOf('\"', p+1);
			if(i==-1)
			{
				return null;
			}
			String token = command.substring(p+1, i);
			p = i+2;
			return token;
		}
		else
		{
			int i = command.indexOf(' ', p);
			if(i==-1)
			{
				String token = command.substring(p, command.length());;
				p = command.length();
				return token;
			}
			else
			{
				String token = command.substring(p, i);
				p = i+1;
				if(token.isEmpty())
				{
					return null;
				}
				else
				{
					return token;
				}
			}
		}
		
		
				
	}
	public String[] getTokens()
	{
		String s;
		List<String> tokens = new ArrayList<String>();
		while((s = nextToken())!=null)
		{
			tokens.add(s);
		}
		return (String[])tokens.toArray(new String[tokens.size()]);
	}
	public static void main(String[] args) {
		String command = "Dette er en \"meget lang test\" 22 \"Trolololo\" \"Mehmeh\" B 20";
		System.out.println(command);
		CommandParser p = new CommandParser(command);
		for (String string : p.getTokens()) {
			System.out.println(string);
		}
	}
}
