import java.util.HashMap;
import java.util.Arrays;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.lang.Object.*;
//name: Wayne Tse
//id:1158718

public class LZpack{
    
    public static void main(String[] args){ 
        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int phraseLength = 0;
            //loop reading in a byte
            ArrayList<Couple> dictionary = new ArrayList<Couple>();
            String inputline;
            while((inputline = br.readLine()) != null){
                    String[] numbers = inputline.split(",");
                    //System.err.println(inputline);
                    //if there are two parts
                    if (numbers.length == 2){
                        //add to dictionary
                        int index = Integer.parseInt(numbers[0]);
                        byte misByte = (byte)Integer.parseInt(numbers[1]);
                        //byteData = (byte)Integer.parseInt(parts[1]);
                        Couple newPair = new Couple(index, misByte);
                        dictionary.add(newPair);
                    }
                    else{
                        //should be last entry then
                        int finalInt = Integer.parseInt(numbers[0]);
                        Couple finalCouple = new Couple(finalInt);
                        dictionary.add(finalCouple);
                    }

            }
            
            //calculate phrase length log2(k)
            int trieCounter = 1;
            
            //round calc up
            
            //foreach couple writeout pack bytes
            //pack them left to right then write
            long writeBuffer = 0;
            int bufIndex = 0;
            int longLength = 64;
            int misBiteLength = 8;

            long misbyteMask = 0x00000000000000FFL;

            for(Couple c : dictionary){

                double calc = Math.log(trieCounter)/Math.log(2);//calulate log2 depending on the trie counter
                phraseLength = Math.max((int)Math.ceil(calc),1);//max to ensure that it does not equal 0
                //calculates phrase length

                long phraseByte = c.Index(); // assume it uses phraselength amount of bits
                long misByte = c.ByteData();// assume it uses 8 bits/1 byte
                
                misByte = misByte & misbyteMask;//removes bits from negatives
                phraseByte = phraseByte << (longLength - phraseLength - bufIndex);
                writeBuffer = writeBuffer | phraseByte; //add phraseByte to writebuffer

                bufIndex += phraseLength;// update bufindex

                //shifts it to apropiate position
                misByte = misByte << (longLength - misBiteLength - bufIndex);
                writeBuffer = writeBuffer | misByte;
                

                bufIndex += misBiteLength;//update bufindex

                long clearMask = 0x00FFFFFFFFFFFFFFL;
                while(bufIndex > 8){
                    //get 8MSB
                    byte outputByte = (byte)(writeBuffer >>> 56);

                    //print it out
                    System.out.write(outputByte);
                    System.out.flush();

                    writeBuffer = writeBuffer << 8;
                    bufIndex -= 8;
                }      
                trieCounter++;
            }
            //print out the rest
            if (bufIndex > 0){
                byte outputByte = (byte)(writeBuffer >>> 56);
                System.out.write(outputByte);
                System.out.flush();
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }  
    }
}

//Couple Class used to holding mismatched bytes and phrase numbers
class Couple{
    private int index;
    private byte byteData;
    private boolean byteExists = false;
    
    public Couple (int index, byte byteData){
        this.index = index;
        this.byteData = byteData;
        byteExists = true;
    }

    public Couple (int index){
        this.index = index;
    }

    public byte ByteData(){
        return byteData;
    }

    public boolean ByteExists(){
        return byteExists;
    }

    public int Index(){
        return index;
    }
}
