//Peter Allen 1199353
//Wayne Tse 1158718

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class REsearcher {

	public static void main(String[] args) {

		ArrayList<Integer> next1 = new ArrayList<Integer>(); //Arraylist for next states
		ArrayList<Integer> next2 = new ArrayList<Integer>();
		ArrayList<String> toMatch = new ArrayList<String>();
		String file = args[0]; //Declare file to be read 
		String line; //String for file reader
		char letter;
		String Input;//String for input reader
		

		try {
			Scanner brInput = new Scanner(System.in);

			while (brInput.hasNextLine()) {
				Input = brInput.nextLine();
				// Populate FSM
				String[] parts = Input.split("\t");
				next1.add(Integer.parseInt(parts[2]));
				next2.add(Integer.parseInt(parts[3]));
				toMatch.add(parts[1]);

			}

			brInput.close();
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}

		try {//Try catch around my buffered reader
			BufferedReader brfilereader = new BufferedReader(new FileReader(file));
			

			while ((line = brfilereader.readLine()) != null) {

				MatchFinder newFinder = new MatchFinder(next1, next2, toMatch);

				if (newFinder.matchFound(line.toCharArray())) {
					System.out.println(line);
				}

			}
			brfilereader.close();

		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find the text File ");
			// If the text file loads inccorectly
			System.err.println(e);// print the error
		} catch (IOException e) {
			System.err.println("Couldn't read from file ");
			// If there is problem reading the file
			System.err.println(e);// print the error
		}
	}

}

class MatchFinder {
	ArrayList<Integer> next1 = new ArrayList<Integer>();
	ArrayList<Integer> next2 = new ArrayList<Integer>();
	Deque<String> deque = new LinkedList<String>();
	ArrayList<String> toMatch = new ArrayList<String>();

	public MatchFinder(ArrayList<Integer> next1, ArrayList<Integer> next2, ArrayList<String> toMatch) {//Constructor for match finder method
		this.next1 = next1;
		this.next2 = next2;
		this.toMatch = toMatch;

	}

	boolean matchFound(char[] inputstring) {//Method to determine whether a match is found
		deque = new LinkedList<String>();
		String scan = "scan";

		deque.add(scan);// add scan

		for (char c : inputstring) {
			deque.push("0");// add initial state
			while (!deque.peek().equals("scan")) {//while the head of deque does not equal scan
				int state = Integer.parseInt(deque.pop());

				if (toMatch.size() - 1 == state) {
					return true;
				}

				// look for next states
				if (toMatch.get(state).equals(String.valueOf(c))) {//If the character in the file matches the FSM
					if (next1.get(state) != next2.get(state)) {//If the next state is not equal to the second
						deque.add(Integer.toString(next2.get(state)));//add next2 to tail
					}
					deque.add(Integer.toString((next1.get(state))));//add next1 to tail

				} else if ((toMatch.size() - 1) == (state)) {//If end of FSM is reached
					return true;//match found

				} else if (toMatch.get(state).equals(" ")) {//If character to match is branching state
					if (next1.get(state) != next2.get(state)) {//If the next state is not equal to the second
						deque.push(Integer.toString(next2.get(state)));//Push next2 to head
					}
					deque.push(Integer.toString((next1.get(state))));//Push next1 to head
				} else if (toMatch.get(state).equals(".")) {//If its a wildcard
					if (next1.get(state) != next2.get(state)) {
						deque.add(Integer.toString(next2.get(state)));//add next2 to tail
					}
					deque.add(Integer.toString((next1.get(state))));//add next 1 to tail

				} else if ((toMatch.get(state).charAt(0) == '^') && (toMatch.get(state).length()) != 1) {//Condition for checking if the character is a backslash
					// get char array

					char[] charArray = toMatch.get(state).toCharArray();//creates an array of everything between brackets
					boolean success = true;
					for (int i = 1; i < charArray.length; i++) {//for loop to search through whats between brackets
						if (charArray[i] == c) {//fail match because file line not supposed to contain what is between brackets
							success = false;
						}
					}

					if (success) {
						if (next1.get(state) != next2.get(state)) {
							deque.add(Integer.toString(next2.get(state)));
						}
						deque.add(Integer.toString((next1.get(state))));

					}

				} else if ((toMatch.get(state).charAt(0) == '\\' && toMatch.get(state).length() == 2)) {//Condition for backslash

					if (toMatch.get(state).charAt(1) == c) {//getting second character of backslash because first is backslash
						if (next1.get(state) != next2.get(state)) {
							deque.add(Integer.toString(next2.get(state)));//add next2 to tail
						}
						deque.add(Integer.toString((next1.get(state))));//add next1 to tail

					}

				}

			}
			deque.pop();//now that scan is on top take it off so we can put it on the tail.Possible states become current states
			deque.add(scan);//add it to tail

		}
		// final check
		while (!deque.peek().equals("scan")) {
			int state = Integer.parseInt(deque.pop());
			if (toMatch.get(state).equals(" ")) {
				if (next1.get(state) != next2.get(state)) {
					deque.push(Integer.toString(next2.get(state)));
				}
				deque.push(Integer.toString((next1.get(state))));

			}

			if (state == toMatch.size() - 1)
				return true;
		}
		return false;	
	}
}
