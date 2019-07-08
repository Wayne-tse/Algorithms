//id:1158718
//name:Wayne Tse

//id::1102424
//name Bernard Harrison
import java.io.*;

///make runs
public class MakeRuns {
	
    ///takes in a string for file name and then integer for run size
	public static void main(String[] args) {
        //int parent, child, child1, child2;
        int runSize;
        runSize = Integer.parseInt(args[1]);
        
        //arrays with the size of the initial runs    
        int count1 = 0;
        int count2 = 0;
		
		// Open the file and store the lines into an array
		try {
			BufferedReader file = new BufferedReader(new FileReader(args[0]));
             String[] array1 = new String[runSize];
            String line;
            
			while (((line = file.readLine()) != null) && (count1 < runSize)) {
				//array.add(line);
                array1[count1] = line;
                count1++;			           
            }
            //sort first run
            HeapSort HS = new HeapSort();
            HS.sort(array1, count1);
            BufferedWriter writer = new BufferedWriter(new FileWriter("temp1.txt"));
            for (int i = 0; i < count1; i++) {
				writer.write(array1[i]);
                writer.newLine();
			}
            writer.close();
            
            //clear for Garbage collection
            array1 = null;
            
            String[] array2 = new String[runSize];
            while (((line = file.readLine()) != null) && (count2 < runSize)) {
				//array.add(line);
                array2[count2] = line; 
                count2++;
			}
            //sort second run
            HS.sort(array2, count2);
            writer = new BufferedWriter(new FileWriter("temp2.txt"));
                
            for (int i = 0; i < count2; i++) {
				writer.write(array2[i]);
                writer.newLine();
			}
            writer.close();
			// Close the file
			file.close();
					
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file");
		
		} catch (IOException ex) {
			System.out.println("Error reading file");
		}

    }	
}
///heapsort class use sort method to sort a string of Arrays.
class HeapSort {
    
    public void sort(String array[]){
        
        int n = array.length;
        //ensures largest value is at root and all parent nodes bigger than child nodes
        //starts from bottom and moves big values up.
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i); 
        }
        //starts decreasing array sort area as rest of array is sorted.
        for (int i = n - 1; i>= 0; i--)
        {
            //swaps largest value to sorted side
            String temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            //put largest value on root again
            heapify(array,i,0);
        }
        //printArray(array);     
    }
    /// overload incase array length incase the original array does not match 
    public void sort(String array[] , int arraylength){
        
        int n = arraylength;
        //ensures largest value is at root and all parent nodes bigger than child nodes
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i); 
        }
        //starts decreasing array sort area as rest of array is sorted.
        for (int i = n - 1; i>= 0; i--)
        {
            String temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            
            heapify(array,i,0);
        }
        //printArray(array);     
    }
    
    /// for testing purposes
    private static void printArray(String array[]){
        System.out.println("Printing Array:");
        int n = array.length;
        for (int i = 0; i < n; i++)
                System.out.println(array[i]);
    }
    
    ///for testing and visualising purposes
    private static void printTree(String array[]){
        System.out.println("Printing Tree:");
        int n = array.length;
        int level = 1;
        int count = 1;
        for (int i = 0; i < n; i++){

        System.out.print(array[i]  + "\t");
        if (level == count)
        {
            count = 1;
            System.out.println();   
            level = level * 2;
        }
        else
        {
            count++;
        }

        }
        System.out.println(); 
    }
    
    private void heapify(String array[], int size, int index){
        //create indexes for parents and child nodes.
        int bigIndex = index;
        int leftIndex = 2 * index + 1;
        int rightIndex = 2 * index + 2;
            
        // if left child bigger than swap indicate that as the largest.
        if(leftIndex < size && array[leftIndex].compareTo(array[bigIndex]) > 0)
            bigIndex = leftIndex;
        
        
        // if right child bigger than reference it as the largest out of the three nodes 
        if(rightIndex < size && array[rightIndex].compareTo(array[bigIndex]) > 0)
            bigIndex = rightIndex;
        
        if(bigIndex != index){
            //printTree(array);//tesing
            String tempSwap = array[index];
            array[index] = array[bigIndex];
            array[bigIndex] = tempSwap;
            
            //printArray(array);//testing
            
            heapify(array, size, bigIndex);
        }
        
    }
}