package abstractJavaHouse;

import java.util.LinkedList;

import java.util.Queue;

/*
	The Contractor class has a queue of various houses that they need to do work on, because the queue is created using abstract classes, they each can have the same function called and executed.
*/

public class Contractor {

	
	Queue<abstractHouse> workQueue;	//queue of houses to build
	double wallet;					//amount of money the contractor has earned
	int jobCounter;					//counter of the number of jobs that have been completed
	
	/*
	 * Default constructor, initializes the workQueue
	 */
	Contractor(){
		
		workQueue = new LinkedList<abstractHouse>();	//initialize the workQueue
		wallet = 0;										//initialize the wallet to 0
		jobCounter = 0;									//initialize the jobCounter to 0
		
	}
	
	/*
	 * returns a string summarizing all the work and profit
	 */
	String summarizeWork(){
		
		return "I completed " + jobCounter + " jobs and earned " + wallet + " dollars!";
		
	}
	
	/*
	 * returns a random boolean value
	 */
	boolean randomBool(){
		
		switch((int)(Math.random() * 100) % 2){
		
			case 0:	return false;
			default:return true;
			
		}
		
	}
	
	/*
	 * returns a random boolean, more likely to return false
	 */
	boolean advertiseContract(){
		
		if (workQueue.size() < 3 && (int)Math.floor((Math.random() * 100) % 7) == 0){
		
			return true;
		
		}
		return false;
	}
	
	/*
	 * makes a new random job- either a trailer or mansion, and adds it to workQueue
	 */
	void newJob(){

		int tempVal = (int) ((Math.random() * 10) % 2) ;
		int tempWheels;
		int tempRooms;
		int tempFloors;
		
		switch(tempVal){
			
			//new Trailer
			case 0:	tempWheels = (int)(Math.floor((Math.random() * 5)));
				tempRooms = (int)(Math.floor((Math.random() * 10) % 3));
				workQueue.add(new Trailer(tempWheels, tempRooms));
				jobCounter++;
				System.out.println("\nGot a new Job building a " + tempWheels + " wheeled, " + tempRooms + " room Trailer!");
				break;
			
			//new Mansion
			case 1: tempRooms = (int)(Math.floor((Math.random() * 10) % 3));
				tempFloors = (int)(Math.floor((Math.random() * 100) % 20));
				workQueue.add(new Mansion(tempRooms, tempFloors, randomBool(), randomBool()));
				jobCounter++;
				System.out.println("\nGot a new Job building a " + tempFloors + " floored, " + tempRooms + " room Mansion!");
				break;
				
			default:System.out.println("Error in switch statement");
		
		}
		
	}
	
	/*
	 * workLeft returns true if there is still houses left in the queue
	 */
	boolean workLeft(){
		
		return !workQueue.isEmpty();
		
	}
	
	/*
	 * doJob performs 1 unit of work on the house at the front of the queue, removes it once finished
	 */
	void doJob(){
		if (workQueue.peek().doWork()){
			
			wallet += workQueue.peek().value;
			workQueue.remove();
		}
		
	}

	
	public static void main(String args[]){
		
		Contractor myContractor = new Contractor();
		
		myContractor.newJob();	//initial job for the contractor
		
		while (myContractor.workLeft()){
			
			myContractor.doJob();//work on the current job
			
			if (myContractor.advertiseContract()){	//if successful in advertising a new job
				
				myContractor.newJob();	//add a new job to the queue
			}
			
		}
		
		System.out.println(myContractor.summarizeWork());
		
		
	}
	
	
}
