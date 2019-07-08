//Peter Allen 1199353
//Wayne Tse 1158718

import java.util.*;
import java.io.*;

/// output:
/// from bottom to top width, length, height

//implement either simulated annealing

public class NPStack{



	public static void main(String[] args){

		double coolingRate = 1;
		double minTemp = 1;

		Random rand = new Random();



		//Checks inputs from command line are correct

		BoxList unusedBoxes = new BoxList();//Needs argument length of 2
		if(args.length != 2){
			System.err.println("Please input two arguments the text file and maximum amount of iterations");
			return;
		}

		if (!isNumeric(args[1])){//Checks for incorrect input i.e letters or symbols
			System.err.println("Please input a valid maximum amount of iterations");
			return;
		}
			//Initialize temp and cooling rate
		double it = Double.parseDouble(args[1]);
		double temp = minTemp + it * coolingRate;
			//checks if double is too big
		if (Double.isInfinite(temp)){
			System.err.println("Amount of iterations is too high for current cooling rate due to double limitations");
			return;
		}
		double maxTemp = temp;
		String file = args[0];
		String line = null;
		try{
			//Buffered reader to read text file
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null){
				String parts[] = line.split(" ");
				//assume 3 parts to in parts
				//Checks line of file are of correct size
				if (parts.length != 3){
					System.err.println("file format error");
				}
				//checks if all parts are numers greater than 1
				int a = Integer.parseInt(parts[0]);
				int b = Integer.parseInt(parts[1]);
				int c = Integer.parseInt(parts[2]);
				if ((a < 1)|| (b < 1) || (c < 1)){
					System.err.println("incorrect dimension, dimensions cannot be negative or zero");
					return;
				}//Make a box
				Boxes newBox = new Boxes(a, b, c);
				unusedBoxes.addBox(newBox);//Add boxes to a list
			}
		}
		catch (Exception e){
			System.err.println(e);//If reader fails throw exception
		}

		BoxList best = unusedBoxes.bestStack().copy();//best is copy of the unused boxlist with best stack called on it. 
		int bestHeight = best.getStackHeight();//initialise best height as the "best" boxlist height

		BoxList currentStack = unusedBoxes.copy();//Initialise current stack as a copy of unusedboxes

		while (temp>minTemp){//While cooling is in process

			
			int currentHeight = currentStack.bestStack().getStackHeight();
			BoxList newStack = currentStack.copy();


			//amount of changes we do = temp/maxtemp * unusedbox.size() 

			int changes = Math.max((int)(temp/maxTemp * unusedBoxes.boxTower.size()), 1);

			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i=0; i<newStack.boxTower.size(); i++) {
				list.add(new Integer(i));//make a list of initegers
			}
			Collections.shuffle(list);//Mix up the integers
			for (int i=0; i<changes; i++) {
				newStack.getBox(i).randOr(rand);//boxes are added to newstack in accordance to the mixed up integers. randOr mixing up their orientations
			}

			int newHeight = newStack.bestStack().getStackHeight();




			if(acceptanceProbability(currentHeight, newHeight, temp) > Math.random()){
				currentStack = newStack.copy();//assign the current stack into the new possible solution
			}
			if(bestHeight < newHeight){//If the new height is greater than the old
				best = newStack.copy();//Make a best a copy of our new stack
				bestHeight = best.bestStack().getStackHeight();//make best height the height of our newstack
			}

			temp -= coolingRate;//reduce temp by cooling rate

		} 

		System.out.println(best.bestStack().toString());//print out stack
	}
	public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
        // If the new solution is better, accept it
		if (newEnergy < energy) {
			return 1.0;
		}
                // If the new solution is worse, calculate an acceptance probability
		return Math.exp((energy - newEnergy) / temperature);
	}

	public static boolean isNumeric(String str) { //To check if string is a number
		try {  
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	}





}

class Boxes implements Comparable<Boxes>{
	int[] dimensions =  new int[3];

	//if orientation = 0 let dimension[0] be height width = dimesnsion[1] length = dimension[2]
	//if orientation = 1 let dimension[1] be height width = dimesnsion[2] length = dimension[0]
	//if orientation = 2 let dimension[2] be height width = dimesnsion[0] length = dimension[1]
	int orientation = 0;
	//if inverse is true swap the width and length
	boolean inverse = false;
	
	//Thus overall there are 6 possible oreintations for our box

	public Boxes(int a, int b , int c){//constructor for boxes
		dimensions[0] = a;
		dimensions[1] = b;
		dimensions[2] = c;
	}

	public Boxes(int a, int b, int c, int o, boolean i){//overload constructor
		dimensions[0] = a;
		dimensions[1] = b;
		dimensions[2] = c;
		orientation = o;
		inverse = i;
	}

	//changes the orientation 
	public void randOr(Random rand){
		int n = rand.nextInt(2) + 1;

		orientation += n;

		if (orientation > 2){
			orientation -= 3;
		} 
	}

	public Boxes copy(){//makes a copy of a box
		return new Boxes(dimensions[0], dimensions[1], dimensions[2], orientation, inverse);
	}

	public int height(){//get height
		return dimensions[orientation];
	}

	public int width(){//get width
		if (inverse){
			if (orientation == 0){
				return dimensions[2];
			}
			return dimensions[orientation - 1];
		}
		else{
			if (orientation == 2){
				return dimensions[0];
			}
			return dimensions[orientation + 1];
		}

	}

	public int length(){//get length
		if (inverse){
			if (orientation == 2){
				return dimensions[0];
			}
			return dimensions[orientation + 1];

		}
		else{
			if (orientation == 0){
				return dimensions[2];
			}
			return dimensions[orientation - 1];
		}
	}



	public void Orientate(int value){//Orients the box		
		int i = 0;
		while(i < dimensions.length){

			if (dimensions[i] == value){
				
				orientation = i;
				if (this.width() > this.length()){
					inverse = !inverse;
				}
				break;
			}

			i++;

		}
		makeWidthLow();
	}
	
	public void makeWidthLow(){//Makes the low value width
		if (this.length() < this.width()){
			inverse = !inverse;
		}
	}

	//to only compare width and length faces only
	@Override
	public int compareTo(Boxes comp){
		int compareSides = comp.width() + comp.length();
		return this.width() + this.length() - compareSides;	
	}

	@Override
	public String toString(){
		String sides;
		// return width length height
		sides = this.width() + " " + this.length() + " " + this.height();
		return sides;
	}
}
///Class to hold a list of box objects
class BoxList{
	ArrayList<Boxes> boxTower = new ArrayList<Boxes>();
	boolean largest = false;
	public int getStackHeight(){//get the height of the stack
		int output = 0;
		for (Boxes temp: boxTower){
			output += temp.height();
		}
		return output;
	}

	/// tests if tower meets faceCondition for testing
	public boolean faceCondition(){
		int i = 0;
		while(i < boxTower.size() - 2){

			if(boxTower.get(i).width() < boxTower.get(i + 1).width()){
				if (boxTower.get(i).length() < boxTower.get(i + 1).length()){

				}
				else {
					return false;				
				}
			}
			else 
			{
				return false;
			}

			i++;

		}
		return true;
	}
	

	public BoxList copy(){//make a list of copied boxes

		BoxList newBoxList = new BoxList();

		for (Boxes b : boxTower){
			newBoxList.addBox(b.copy());
		}

		return newBoxList;

	}
	public void addBox(Boxes b){//add a box to a boxlist
		boxTower.add(b);
	}
	public void insertBox(int n,Boxes b){//insert a box into a list
		boxTower.add(n,b);
	}
	public void setBox(int index, Boxes box){//Set a box to an index in a boxlist
		boxTower.set(index,box);
	}

	public Boxes getBox(int n){//get a box

		if (n >= boxTower.size()){
			return 	null;	
		}
		return boxTower.get(n);
	}

	public void removeBox(int n){//remove a box
		boxTower.remove(n);
	}


	public void sortSmall(){//sort the boxlist with the smallest becoming width and 2nd smallest becoming length
		Collections.sort(boxTower);
	}



	public void makeWidthLowAll(){//make all widths low in the box list
		for(Boxes b :boxTower){
			b.makeWidthLow();
		}
	}



	//makes best stack
	public BoxList bestStack(){
		makeWidthLowAll();
		sortSmall();
		BoxList uniqueList = copy();
		BoxList newBoxList = new BoxList();
		newBoxList.addBox(getBox(0));
		int i = 0;
		
		while (i < uniqueList.boxTower.size()){
			if(uniqueList.getBox(i).width() > newBoxList.getBox(newBoxList.boxTower.size()-1).width()){
				if(uniqueList.getBox(i).length()>newBoxList.getBox(newBoxList.boxTower.size()-1).length()){
					newBoxList.addBox(uniqueList.getBox(i));
					uniqueList.removeBox(i);
					i--; 

				}
			}
			i++;
		}
		return newBoxList;

	}


	//toString method returns width, length, height, sum(height) for boxtower
	@Override
	public String toString(){
		String stringReturn = "";
		int i = boxTower.size() - 1;
		int totalH = 0;

		while (i >= 0){
			totalH += getBox(i).height();
			stringReturn += getBox(i).toString() + " " + totalH + "\n";		
			i--;
		}
		return stringReturn;
	}	



}
