import java.io.BufferedWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.xml.bind.DatatypeConverter;

public class RCImplementor {
	public static int w=32;
	public static int r=20;
	public static int Pw=0xb7e15163;
	public static int Qw=0x9e3779b9;
	int A=0;
	int B=0;
	int C=0;
	int D=0;
	BufferedWriter writer;
	String output=new String();
	Writer write=new Writer();
	int counter=0;
	int[] subKeys;
	byte[] text;
	byte[] key;
	
	public void preprocessor(String textStr,String userkey,int operation,String outputfile)
	{
			if(userkey.length()<w)
			{
				while(userkey.length()<w)
				{
					userkey.concat("0");
				}
				System.out.println("key updated to:"+userkey);
			}
			text=new byte[textStr.length()];
			text=DatatypeConverter.parseHexBinary(textStr);
			key=new byte[userkey.length()];
			key=DatatypeConverter.parseHexBinary(userkey);
			subKeys=createSubKeys(key);
			if(operation==1)
			{
				encrypt(text,subKeys,outputfile);
			}
			else if(operation==2)
			{
				decrypt(text,subKeys,outputfile);
			}
	}
	private void createOutput(int input)
	{
		
		byte[] buff = ByteBuffer.allocate(4).putInt(input).array();
        for (int i=buff.length-1;i>=0;i--) 
        {
        	output=output + String.format("%02x ", buff[i]);
        }
	}

	
	public void encrypt(byte[] plainText, int[] s,String outputfile)
	{
		counter=0;
		int t=0,u=0;
		A=0;
		B=0;
		C=0;
		D=0;
			A=convertTOLittleEndian(plainText);
			B=convertTOLittleEndian(plainText);
			C=convertTOLittleEndian(plainText);
			D=convertTOLittleEndian(plainText);
			B=B+s[0];
			D=D+s[1];
			for(int i=1;i<=r;i++)
			{
				t=rotateL((B*(2*B+1)),5);
		        u=rotateL((D*(2*D+1)),5);
		        A=rotateL((A^t),u)+s[2*i];
		        C=rotateL((C^u),t)+s[2*i+1];
		        swapEncrypt();
			}
			A = A + s[2 * r + 2];
			C=  C + s[2 * r + 3];
			
			createOutput(A);
			createOutput(B);
			createOutput(C);
			createOutput(D);
			System.out.println(output);
			write.writeToFile(output, outputfile,1);
	}
	public void decrypt(byte[] cipherText,int[] s,String outputfile)
	{
		counter=0;
		int t=0,u=0;
		A=0;
		B=0;
		C=0;
		D=0;
			A=convertTOLittleEndian(cipherText);
			B=convertTOLittleEndian(cipherText);
			C=convertTOLittleEndian(cipherText);
			D=convertTOLittleEndian(cipherText);
		
			C = C - s[2*r+3];
			A = A - s[2*r+2];
			for(int i = r;i>=1;i--)
			{
				swapDecrypt();	
				u = rotateL(D*(2*D+1),5);
				t = rotateL(B*(2*B + 1),5);
				C = rotateR(C-s[2*i + 1],t) ^ u;
				A = rotateR(A-s[2*i],u) ^ t;  
	                            
			}
			D = D - s[1];
			B = B - s[0];
			
			
			createOutput(A);
			createOutput(B);
			createOutput(C);
			createOutput(D);
			System.out.println(output);
			write.writeToFile(output, outputfile,2);

	}

	private int[] createSubKeys(byte[] key)
	{
		int i=0,j=0,cnt=0;
		int v;
		int a=0,b=0;
		int u = w/8;				//
		int c = key.length/u;		//
		int t = 2 * r + 4;      	//Upper limit for subkey array.
		int[] s = new int[t];
		int[] L = new int[c];
		for(int iterator=0;iterator<key.length;iterator=iterator+4)
		{
		     L[cnt++]=convertTOLittleEndian(key);
		}
		s[0] = Pw;
		for( int counter=1;counter<t;counter++)
		{
		 	s[counter]=s[counter-1]+Qw;
		}
		v = 3 * Math.max(c, t);
		for(int counter=0;counter<v;counter++)
		{
			a = s[i]=rotateL((s[i] + a + b), 3);
			b = L[j]=rotateL((L[j] + a + b),(a + b));
			i = (i + 1) % t;
			j = (j + 1) % c;
		}
		return s;
	}
	private int convertTOLittleEndian(byte[] arr)
	{
		int cnt;
		byte[] convert=new byte[4];
		cnt=counter;
		try
		{
			for(int i=0;i<4;i++)
			{
				convert[i]=arr[cnt++];
			}
			counter=cnt;
			int x = ByteBuffer.wrap(convert).order(ByteOrder.LITTLE_ENDIAN).getInt();
			return x;
		}catch(ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	private void swapEncrypt()
	{
		int swapper=0;
		swapper=A;
        A=B;
        B=C;
        C=D;
        D=swapper;
	}
	private void swapDecrypt()
	{
		int swapper=0;
		swapper = D;
		D = C;
		C = B;
		B = A;
		A = swapper;
	}
	private int rotateL(int number, int offset) 
	{
	 	int ret = (number << offset) | (number >>> (32-offset));
    	return ret;
	}
    
    private int rotateR(int number, int offset) 
    {
    	int ret = (number >>> offset) | (number << (32-offset));
    	return ret;
	}
}
