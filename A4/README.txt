Name: Wayne Tse, Peter Allen
Id: 1158718, 1199353

NPStack.java

takes in two arguments into the commands 
- file where boxes are described
- the amount of epochs/iterations to be run

This application is using Simulated Anealing to try and find a good solution.
To Simulate the anealing process we randomised the orientation on every epoch and then tried to the tallest box stack from this list.

It was possible to find the tallest stack possible using logic, with other methods. However, this would of defeated the point of this application as it would no longer have been trying to simulate anealling via epochs.

Cooling rate was decided to be done in a linear fashion instead of a multiplicative fashion as it allowed for higher amount of iterations that is not restricted by the double limits and made it do more changes at the higher temps which seemed to have a postive effect of the outcome.