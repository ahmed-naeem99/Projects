

/*TODO: centralize main function into one class, have electrician and contractor work together,
 * Job sets
*/


package abstractJavaHouse;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

abstract class abstractWorker {
    // Common properties for all workers
    Queue<abstractHouse> workQueue;
    double wallet;
    int jobCounter;
    int workRequired;
	int workCounter;

    // Constructor
    abstractWorker() {
        workQueue = new LinkedList<>();
        wallet = 0;
        jobCounter = 0;
    }
    
 // This is an assumption
 	static final int NumOfWheelsPerRoom = 2;
 	static final int minTrailerRooms = 1;
 	static final int maxTrailerRooms = 2;
 	static final int minMansionFloors = 1;
 	static final int maxMansionFloors = 4;
 	static final int minNumberOfRoomsPerFloor = 1;
 	static final int maxNumberOfRoomsPerFloor = 10;
 	static final int revenueGoal = 100000;

    // Common method to summarize work
    String summarizeWork() {
        return getWorkerType() + " completed " + jobCounter + " jobs and earned " + wallet + " dollars!";
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
	 * Takes a minimum and maximum as arguments and generates a random number in
	 * that range Uses "random" object and then returns a random number
	 */
	public static int generateRanNumber(int min, int max) {
		// Check if the input values are valid
		if (min >= max) {
			throw new IllegalArgumentException("Minimum value must be less than maximum value");
		}

		// Create a Random object
		Random random = new Random();

		// Generate a random number within the specified range
		int randomNumber = random.nextInt((max - min) + 1) + min;

		return randomNumber;
	}
	
	abstract String getWorkerType();
    
	void trailerJobLogic() {
		int tempRooms = generateRanNumber(minTrailerRooms, maxTrailerRooms);
		int tempWheels = tempRooms * NumOfWheelsPerRoom;
		workQueue.add(new Trailer(tempWheels, tempRooms));
		jobCounter++;
		System.out.println("\nGot a new Job building a " + tempWheels + " wheeled, " + tempRooms + " room Trailer.");
	}
	
	void mansionJobLogic() {
		int tempFloors = generateRanNumber(minMansionFloors, maxMansionFloors);
		int tempRooms = generateRanNumber(minNumberOfRoomsPerFloor, maxNumberOfRoomsPerFloor) * tempFloors;
		workQueue.add(new Mansion(tempRooms, tempFloors, randomBool(), randomBool()));
		jobCounter++;
		System.out.println("\nGot a new Job building a " + tempFloors + " floored, " + tempRooms + " room Mansion.");
	}
    
    
    void newJob() {
    	final int TRAILER_CASE = 0;
		final int MANSION_CASE = 1;
    	
    	int trailerOrMansion = (int) (Math.random() * 10) % 2;

		switch (trailerOrMansion) {
		case TRAILER_CASE:
			trailerJobLogic();
			break;

		case MANSION_CASE:
			mansionJobLogic();
			break;

		default:
			System.out.println("Error!");
		}
    }
    
	/*
	 * Creates a job set in a for loop based on the random number generated in the
	 * main() function.
	 */
	void createNewJobSet() {
		// Initialize the first set of jobs
		int numJobs = generateRanNumber(1, 2);

		for (int i = 0; i < numJobs; i++) {
			newJob();
		}
	}
    
    abstract boolean advertiseContract();

    // Common method to check if work is left
	boolean workLeft() {
		return !workQueue.isEmpty();
	}
	
	
	//Does not need to be abstract since it is not class-specific  
	boolean doWork() {
		workCounter++;
		
		if (workCounter < workRequired){
			return false;
		}
		else {
			System.out.println("Done working on a job.");
			return true;
			
		}
		
	}

	// Common method to perform a job
    void doJob() {
        if (workQueue.peek().doWork()) {
            wallet += workQueue.peek().value;
            workQueue.remove();
        }
    }
}




