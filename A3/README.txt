name: Wayne Tse, Peter Allen
id:1158718, 1199353

ASSUMPTIONS:

	it is is ok to send Sting Matches in the FSM and not just char.

	- This is to handle "/(literal)"
	- ^[(list)] 

	FSM can send "/a" and "^(list)"

	where (list) is a list of chars that should not match

	- Assume that ".", "/","^" logic can be handled in REsearcher.java

	Assumed that AA|BB is equivilant to (AA)|(BB) not A(A|B)B due to concatenation having a higher precedence than | listed in the assignemnt specifications

	Assumed FSM states are sperated by \t instead of spaces
	This was done for more clarity and more consistency.
	
