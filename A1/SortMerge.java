//id:1158718
//name:Wayne Tse

//id::1102424
//name Bernard Harrison
import java.io.*;
import java.util.*;

public class SortMerge {
	
	public static void main(String[] args) {
		int i = 0;
        int j = 0;
        int countAccess = 0;
		List<String> array1 = new ArrayList<String>();
		List<String> array2 = new ArrayList<String>();
        String filename1 = "temp1.txt";
        String filename2 = "temp2.txt";
        String outputFile = "output.txt";
		
		// Open the two files and store the lines into arrays
		try {
			BufferedReader file1 = new BufferedReader(new FileReader(filename1));
			BufferedReader file2 = new BufferedReader(new FileReader(filename2));
			String line;
			while ((line = file1.readLine()) != null) {
				array1.add(line);
                
			}
			
			while ((line = file2.readLine()) != null) {
				array2.add(line);
			}
			
			// Close the files
			file1.close();
			file2.close();
					
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file");
		
		} catch(IOException ex) {
			System.out.println("Error reading file");
		}
        
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
           countAccess++;
		
            // Merge the data from the two files
            while (i < array1.size() && j < array2.size()) {

                // Read the next element from each input file
                String element1 = array1.get(i);
                String element2 = array2.get(j);

                // Determine which value is smaller and output it
                if (element1.compareTo(element2) < 0) {
                	writer.write(element1);
                    writer.newLine();
                    System.out.println(element1);
                    i++;
                } else {
                	writer.write(element2);
                    writer.newLine();
                    System.out.println(element2);
                    j++;
                }
            }

            // Output the rest of the first array
            while (i < array1.size()) {
                writer.write(array1.get(i));
                writer.newLine();
                System.out.println(array1.get(i));
                i++;
            }

            // Output the rest of the second array
            while (j < array2.size()) {
                writer.write(array2.get(j));
                writer.newLine();
                System.out.println(array1.get(j));
                j++;
            }
        }
        catch(IOException ex) {
			System.out.println("Error writing file");
		}
        
        System.err.println("File Access:");
        System.err.println(countAccess);
	}
}
