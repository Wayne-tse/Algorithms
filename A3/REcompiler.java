//Wayne Tse 1158718
//Peter Allen 1199353

import java.util.*;
///Phrase Structure Rules
///E -> T
///E -> T E
///E -> T | T
///T -> F
///T -> F*
///T -> F?
///T -> FF
///F -> v
///F -> [vvv]
///F -> ^[vvv]
///F -> .
///F -> /(char)
///F -> (E)


public class REcompiler{


	public static void main(String[] args){


		if (args.length != 1){
			System.err.println("Please input a single string of regex to decode into a finite state machine");
			return;
		}	
		char[] c = args[0].toCharArray();//adds arguments to an array

		GrammarCheck gCheck = new GrammarCheck(c);//method to check for syntax errors in Regexp
		if (gCheck.check()){//If it passes
			Parser parse = new Parser(c);//Parser parses the argument
			parse.parse();
			parse.printStates();//Prints the FSM to standard output
		}
		else{
			System.err.println("failed check");
		}
	}
}

/// Used to ensure that regex is following a specific format
class GrammarCheck{

	char[] p;
	public GrammarCheck(char[] p){
		this.p = p;

	}
/// Call to check 
	public boolean check (){
		for(int i = 0; i < p.length; i++){
			
			if (p[i] == '['){
				//check for closing			 
				int j = 0;
				boolean ok = false;
				i++;

				for (;i < p.length ;i++){
					if (p[i+j] == ']'){

						ok = true;
						break;
					}
				}
				if (!ok){
					//throw error
					error("no ]");
					return false;
				}
			}
			else if (p[i] == '('){
				//check for closing
				int j = 0;
				boolean ok = false;
				j++;//skip 1 as cannot be empty
				for (;i+j < p.length ;j++){
					if (p[i+j] == ')'){
						ok = true;
						break;
					} 
				}
				if (!ok){
					//throw error
					error("no )");
					return false;
				}
			}
			else if (p[i] == '*'){
				
				//check for previous factor
				if (islit(p[i-1]) || (p[i-1] == ']') || (p[i-1] == ')')){
				}
				else {
					error("no previous factor");
					return false;
				}
			}
			else if(p[i] == '?'){
				//check for previous factor
				if (islit(p[i-1]) || p[i-1] == ']' || p[i-1] == ')'){
				}
				else {
					error("no previous factor");
					return false;
				}
			}
			else if(p[i] == '\\'){
				//check for anything after
				if ((i + 1) > p.length){
					error("nothing after \\");
					return false;
				}
				//skip next char
				i++;
			}
			else if(p[i] == '^'){
				i++;
				if (i >= p.length){
					error("no [");
					return false;
				}
				if (p[i]=='['){
					int j = 0;
					boolean ok = false;
					i++;

					for (;i < p.length ;i++){
						if (p[i+j] == ']'){

							ok = true;
							break;
						}
					}
					if (!ok){
					//throw error
						error("no ]");
						return false;
					}
				}
				else {
					error("no [ after ^");
					return false;
				}

			}
			else if(p[i] == ' '){
				error("cannot have \" \"");
				return false;
			}
		}
		return true;

	}

///returns true if not a special char
	boolean islit(char c){
		if ((c == '*') || (c == '?') || (c == '|') || (c == '(') || (c == ')') || (c == '[') || (c == ']') || (c == '^') || (c == '\\') || (c == ' ') || ((int)c == 0))
			return false;
		else
			return true;
	}

	void error(String e){
		System.err.println(e);
		//System.exit();
	}
}
///Class to parse the regexp
class Parser{

	char[] p;
	String[] ch;
	int[] next1;
	int[] next2;
	int state;
	int j;

	public Parser(char[] p){
		this.p = p;
		ch = new String[1];
		next1 = new int[1];
		next2 = new int[1];

	}
	//updates the arrays
	void set_state(int s, String c, int n1, int n2){  
			//make new array size if nessersary
		if (s >= ch.length)
		{
			String[] newch = new String[s+1];
			int[] newNext1 = new int[s+1];
			int[] newNext2 = new int[s+1]; 
			System.arraycopy(ch, 0, newch, 0, ch.length);
			System.arraycopy(next1, 0, newNext1, 0, next1.length);
			System.arraycopy(next2, 0, newNext2, 0, next2.length);
			ch = newch;
			next1 = newNext1;
			next2 = newNext2;
		}
		ch[s] = c;
		next1[s] = n1;
		next2[s] = n2;

	}

	//set state overload to take in chars
	void set_state(int s, char c, int n1, int n2){
		set_state(s, Character.toString(c), n1, n2);
	}
	//finds the expression
	int expression()
	{
		int r = 0;

		r=term();
		//if not end keep going 
		if (j < p.length)
		{ 
			//if not valid next char do not keep going
			if(isvocab(p[j])||(p[j]=='(')||(p[j] == '\\')|| (p[j] =='[')|| (p[j] == '.') || (p[j] == '^'))
			{
				expression();
			}
		}

		return(r);
	}
	//finds the term
	int term()
	{
		int r,t1,t2,f;
		f = state - 1; 
		//find factors
		r = t1 = factor();
		if (j >= p.length)
		{
			return (r); // if end exit
		}
		if(p[j] == '*'){//is next char a closure
			//add state to make previous factor repeatable
			set_state(state," ",state+1,t1);
			j++; 
			r=state;  
			state++;
		}
		else if(p[j] == '?'){//is next char other closure
			if(next1[f] == next2[f]){
				next2[f] = state;
			}
			next1[f] =  state;
			//add states to make previous factor skipable
			set_state(state," ", state+1,t1);
			state++;
			j++;
			if(next1[t1] == next2[t1]){
				next2[t1] = state;

			}
			next1[t1] =  state;
		}
		if (j >= p.length)
		{
				return (r); // if end exit
			}
		else if (isvocab(p[j]) || (p[j] == '.') || (p[j] == '^') || (p[j] == '[') || (p[j] == '\\') || (p[j] == '(')){//is there another term if so concatenate it
				term();//concatenate terms 
			}

			if (j >= p.length)
			{
				return (r); //if end exit
			}
		else if(p[j] == '|'){//lowest precedence so done last
			if(next1[f] == next2[f]){
				next2[f] = state;

			}
			next1[f] =  state;
			f=state-1;
			j++;
			r=state;
			state++; 
			t2=term();
			set_state(r," ",t1,t2);

			if(next1[f] == next2[f]){
				next2[f] = state;
			}
			next1[f] =  state;
			
		}
		return(r);
	}
	//finds and determines the factor
	int factor()
	{
		int r = 0;

		if(isvocab(p[j]) || (p[j] == '.')){//if its a wild card condition or literal
			set_state(state,p[j],state+1,state+1);
			j++;r=state; state++;//go to next
		}
		else
			if(p[j]=='('){//if its a parenthesis
				j++; 
				r = expression();
				if(p[j]==')'){//Whatevers inside is a treated as an expression
					j++;
				}
				else{
					error("factor00");
				}   
			}
			else if(p[j] == '\\'){//If its a backslash
				j++;
				set_state(state,"\\" + Character.toString(p[j]),state+1,state+1);
				j++;r=state; state++;//It and the following character are added to the state

			}
			else if(p[j] == '['){//If its square brackets
				ArrayList<Character> list = new ArrayList<Character>();
				j++;
				list.add(p[j]);
				j++;

				while (p[j] != ']'){//Everything inside square bracket is added to an arraylist
					list.add(p[j]);
					j++;

				}
				char[] charArray = new char[list.size()];

				for (int i = 0; i <list.size(); i++){//made another list so we can find size
					charArray[i] = list.get(i);
				}
				r = sqBrackets(charArray);//expression becomes the entirety of the square brackets
				j++;


			}
			else if(p[j] == '^'){//If carat top
				ArrayList<Character> list = new ArrayList<Character>();
				j++;
				if (p[j] == '['){//Needs to be followed by square brackets
					j++;
					list.add(p[j]);
					j++;
					while (p[j] != ']'){//Loops through until closing bracket is found
						list.add(p[j]);
						j++;
					}

					char[] charArray = new char[list.size()];//Do the same loop as for square bracket.

					for (int i = 0; i <list.size(); i++){
						charArray[i] = list.get(i);
					}
					r = antiSqBrackets(charArray);
					j++;


				}
				else {
					error("factor02");
				}

			}
			else{
				error("factor01");
			}


			return(r);
		}
		void parse()
		{
			int initial;
	  //insert dummy state
			set_state(state,' ',state+1,state+1);
			state++;
			initial=expression();
			if(j < p.length){
	  error("parse00"); // In C, zero is false, not zero is true
	}
	set_state(state," ",0,0);
}

//prints out sqState
int sqBrackets(char[]c){
	int i = 0;
	int r = state;
	int f = state - 1;
	int finalstate = state + 1 + 2 *(c.length - 1);
	if (isvocab(c[i])){
		set_state(state, c[i],finalstate, finalstate);
	}
	else {
		set_state(state, "\\" + c[i] , finalstate, finalstate);
	}

	int t1 = state;
	int t2;
	state++; i++;
	boolean doOnce = true;

	while(i < c.length){
		f = state-2;
		if (next1[f] == next2[f]){
			next1[f] = state;
		}
		next2[f] = state;

		f = state-1;
		t2 = state + 1;
		set_state(state," ",t1,t2);//branch into 2nd term and first term
		if (doOnce){
			doOnce = false;
			r = state;//begining of branching state
		}

		state++;

		// add in 2nd term
		if (isvocab(c[i])){
			set_state(t2, c[i],finalstate, finalstate);
		}
		else {
			set_state(t2, "\\" + c[i] , finalstate, finalstate);
		}

		state++;
		t1 = t2;//2nd term becomes new term 1 on next loop
		i++;
	}

	set_state(finalstate," ", state+1, state+1);
	state++;

	return r;

}
//prints out antiSQ state
int antiSqBrackets(char[] c){
	int r = state;
	String output = "^" + new String(c);
	set_state(state,output, state + 1, state + 1);
	state++;
	return r;
}

void error(String e){
	System.err.println(e);
}
//check if it not a special char
boolean isvocab(char c){
	if ((c == '.') || (c == '*') || (c == '?') || (c == '|') || (c == '(') || (c == ')') || (c == '[') || (c == ']') || (c == '^') || (c == '\\') || (c == ' ') || ((int)c == 0))
		return false;
	else
		return true;
}
//outputs all the states
void printStates(){
	for(int i = 0; i < ch.length; i++)
	{
		System.out.println(i + "\t" + ch[i] + "\t" + next1[i] + "\t" + next2[i]);
	}
}
}
