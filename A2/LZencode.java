import java.util.HashMap;
import java.util.Arrays;

import java.util.*;
import java.util.Scanner;
import java.io.*;
import java.lang.Object.*;
//name: Wayne Tse
//id:1158718
public class LZencode{
    
    ///assumptions: 
    ///    - that the input file is put into the arguments
    ///    - the output is to put into system.out.print standard output
    ///    - the output is to be put in "index, byte\n" format
    ///    - the output is in integer, integer format for consistancy and readabilility to nicely handle new line chars

    public static void main(String[] args){   
        //beginning of trie root
        MultiTrieNode root = new MultiTrieNode();       
        
        try{            
            //reading fields and input stream
            int input;
            InputStream is = new BufferedInputStream(System.in);
            
            //start go through tree via the root            
            MultiTrieNode node = root;
                //loop to go through file
                while(true){
                    //read a byte
                    input = is.read();
                    //if -1 then end of file
                    if(input == -1){
                        //break out of loop
                        if (node.Index() == 0){
                        break;
                        }
                        else{
                        System.out.println(node.Index() + "," + "0");
                        break;
                        }
                    }
                    if (node.find((byte)input) == null){
                        //node not found add entry
                        //add node to this hashmap
                        node.add((byte)input);
                        //output
                        System.out.println(node.Index() + "," +  (byte)input);
                        node=root;
                        //reset node
                    }
                    else{ 
                        //node found look one level deeper in trie
                        node = node.find((byte)input);
                    }         
                }              
            is.close();          
        }
        catch (FileNotFoundException ex) {
			System.err.println(ex);
		 
        }
        catch (IOException ex) {
            System.err.println(ex);
        }    
        
    }
  
}


//multi trie class
class MultiTrieNode{
    private static int trieCount = 0;
    private byte c;
    private int index;
    private HashMap<Byte, MultiTrieNode> children = new HashMap<Byte, MultiTrieNode>();
    
    //initalisation overloads
    public MultiTrieNode() {
        this.index = trieCount;
        trieCount++;
    }
    
    public MultiTrieNode(int c){
        this.c = (byte)c;
        this.index = trieCount;
        trieCount++;
    }
    
    public MultiTrieNode(char c){
        this.c = (byte)c;
        this.index = trieCount;
        trieCount++;
    }


    public MultiTrieNode(byte c){
        this.c = c;
        this.index = trieCount;
        trieCount++;
    }
    //search function if cannot find it will return null
    public MultiTrieNode find(byte fkey){
        //if true map contains what we are looking for
        if (children.containsKey(fkey)){
            return children.get(fkey);
        }
        return null;
    }
    
    //adds node to hash table if it does not exist in current hash
    public void add(byte akey){

        MultiTrieNode newChild = new MultiTrieNode(akey);
        children.putIfAbsent(akey, newChild);        
    }
    
    //return int index
    public int Index() {
        return index;
    }
}