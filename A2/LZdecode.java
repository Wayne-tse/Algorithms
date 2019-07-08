import java.util.HashMap;
import java.util.Arrays;


import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.lang.Object.*;
//name: Wayne Tse
//id:1158718
public class LZdecode{
     
    ///assumptions: 
    ///    - the input is to be put in "index, byte as an int" format
    ///    - the input can be "index, " format
    public static void main(String[] args){   
        
        //dictionary list that is going to be created
        ArrayList<Couple> dataList = new ArrayList<Couple>(); 
        
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String inputline;

            while((inputline = br.readLine()) != null){           
                String[] parts = inputline.split(",");
                
                int index = Integer.parseInt(parts[0]);   
                List<Byte> byteDecode = new ArrayList<Byte>();

                //checks if there is a second argument in the line
                Couple Entry = new Couple(index); 
                if (parts.length == 2){
                    byte byteData = (byte)Integer.parseInt(parts[1]);
                    Entry = new Couple(index, byteData);  
                }                         
                dataList.add(Entry);

                //if there is byte
                if (Entry.ByteExists()){

                byteDecode.add(Entry.ByteData());
                }
    
                //create statement loop
                int indexCounter = Entry.Index();

                while(indexCounter != 0 ){
                    //-1 as trie was 0 based but array list is not
                    Entry = dataList.get(indexCounter - 1);
                    indexCounter = Entry.Index();
                    if (Entry.ByteExists()){
                    byteDecode.add(Entry.ByteData());
                    }
                }                
                //write the bytes out in inverse order
                for (int i = byteDecode.size(); i-- > 0; ) {
                    System.out.write(byteDecode.get(i));
                    System.out.flush();//flush to actually right bytes
                }
            }
            br.close(); 
            
        }
        catch (FileNotFoundException ex) {
			System.err.println(ex);
		 
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