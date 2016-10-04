import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Driver 
{
	static String line;
	static String plaintext;
	static String userkey;
	static String ciphertext;
	static String op;
	public static void main(String[] args) 
	{
			try
			{
		
				RCImplementor rc6=new RCImplementor();
				op=fileRead(args[0]);
				if(op.equalsIgnoreCase("Encryption"))
				{
					rc6.preprocessor(plaintext, userkey,1,args[1]);
				}
				else if(op.equalsIgnoreCase("Decryption"))
				{
					rc6.preprocessor(ciphertext, userkey,2,args[1]);
				}
				else
				{
					throw new Exception();
				}
			}catch(Exception e)
				{
					System.out.println("problem while processing the string..program will terminate");
					System.err.println("problem while processing the string");
					e.printStackTrace();
					System.exit(0);	
				}
		}
	private static String stringProcessor(String str)
	{
		String[] buffer;
		String temp="";
		buffer=str.split(" ");
		for(int i=1;i<buffer.length;i++)
		{
			temp=temp+buffer[i];
		}
		return temp;
	}
	private static String fileRead(String inputfile)
	{
		String operation=new String();
		try
		{
			FileReader freader=new FileReader(inputfile);
			BufferedReader reader=new BufferedReader(freader);
			line =reader.readLine();
			if(line.equalsIgnoreCase("Encryption"))
			{
				operation=line;
				plaintext=reader.readLine();
				userkey=reader.readLine();
				plaintext=stringProcessor(plaintext);
				
			}
			else if(line.equalsIgnoreCase("Decryption"))
			{
				operation=line;
				ciphertext=reader.readLine();
				userkey=reader.readLine();
				ciphertext=stringProcessor(ciphertext);
			}
			userkey=stringProcessor(userkey);
			reader.close();
			freader.close();
			return operation;
		}catch(FileNotFoundException e)
		{
			System.out.println("File not found..");
			System.err.println("File not found..");
			System.exit(0);
		}
		catch(IOException e)
		{
			System.out.println("IO exception..program will terminate");
			System.err.println("IO exception..");
			System.exit(0);
		}
	
		return null;
	}

}
