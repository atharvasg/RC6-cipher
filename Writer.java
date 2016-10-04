import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer 
{
	File file;
	BufferedWriter writer;
	FileWriter fwriter;
	public void writeToFile(String str,String outputfile,int operation)
	{
		try 
		{
			file=new File(outputfile);
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			fwriter=new FileWriter(file);
			writer=new BufferedWriter(fwriter);
			if(operation==1)
			{
				
				writer.write("ciphertext:"+ str);
			}
			else if(operation==2)
			{
				
				writer.write("plaintext:"+ str);
			}
			//fwriter.close();
			writer.close();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
