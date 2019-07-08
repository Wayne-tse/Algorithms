///assume bits => n * (log(n) + mismatched bits)
/// bits => n * log(n) + 8n

import java.util.HashMap;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.lang.Object.*;
//name: Wayne Tse
//id:1158718
public class LZunpack{
    
    public static void main(String[] args){ 
        try{
        	int input;
        	InputStream is = new BufferedInputStream(System.in);

        	//need to figure out phraselength
        	ArrayList<Integer> byteList = new ArrayList<>();

        	long buffer = 0;
        	int bufferIndex = 0;
        	int phraseLength = 0;
        	int longLength = 64;
        	int byteLength = 8;
        	int trieCounter = 1;
        	int misByteLength = 8;

        	long byteMask = 0x00000000000000FFL;
        	
        	while(true){
        	//fill up buffer
	        	while ((bufferIndex < (longLength - byteLength)) && ((input = is.read()) != -1)){
	        		long newLong = input;
	        		//get rid of the negatives
	        		newLong = newLong & byteMask;
	        		newLong = newLong << (longLength - bufferIndex - byteLength);
	        		buffer = buffer | newLong;
	        		bufferIndex += byteLength;
	        	}

	        	double calc = Math.log(trieCounter)/Math.log(2);
                phraseLength = Math.max((int)Math.ceil(calc),1);
                //calculate phraseLength
                long phrase = buffer >>> (longLength - phraseLength);
                buffer = buffer << phraseLength;
                bufferIndex -= phraseLength;
                long misByte =  buffer >>> (longLength - misByteLength);
                buffer = buffer << misByteLength;
                bufferIndex -= misByteLength;

                trieCounter++;
                System.out.println((int)phrase + "," + (byte)misByte);

                if (bufferIndex < (phraseLength + misByteLength)){
                	//break if there are no more couples
                	break;
                }
        	}
        	
        	if (bufferIndex > 0){
        		long phrase = buffer >>> (longLength - phraseLength);
        		if (phrase != 0){
        			System.out.println((int)phrase+ ",");
        		}
        	}
        	is.close();

    	}
        catch (IOException ex) {
            System.err.println(ex);
        }  
    }
}
