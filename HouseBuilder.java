/*
 * Name: Ahmed Naeem
 * Date: Jan 20, 2024
 * Description: This program represents a generic construction management system using abstract classes.
 *              By introducing abstract classes for different house types, the program becomes more versatile.
 *              The main class, Electrician, manages construction jobs, including both generic houses and specific types like Mansion and Trailer.
 *              The way the program works is that it takes a random number of jobs (between 1 to 5 jobs) and completes those jobs, and continues to do so until the money goal is reached.
 * 
 * Input: The program doesn't take user input in this version. It generates construction jobs randomly.
 * Output: The program outputs the progress of construction jobs and the total earnings after completing all jobs.
 * TO DO: fix magic numbers
 */

package abstractJavaHouse;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/*
 * Represents a Electrician who manages construction jobs for Mansion and Trailer house types.
 */
class HouseBuilder {
	// Queue to manage construction jobs.
	static Queue<abstractHouse> workQueue;
	static double wallet;
	static int jobCounter;

	// This is an assumption
	static final int NumOfWheelsPerRoom = 2;
	static final int minTrailerRooms = 1;
	static final int maxTrailerRooms = 2;
	static final int minMansionFloors = 1;
	static final int maxMansionFloors = 4;
	static final int minNumberOfRoomsPerFloor = 1;
	static final int maxNumberOfRoomsPerFloor = 10;
	static final int revenueGoal = 100000;

	HouseBuilder() {
		// Initialize the workQueue as a LinkedList.
		workQueue = new LinkedList<>();
		wallet = 0;
		jobCounter = 0;
	}

	// Summarizes the completed jobs and earnings.
	String summarizeWork() {
		return "I completed " + jobCounter + " jobs and earned " + wallet + " dollars!";
	}

	// Generates a random boolean value.
	static boolean randomBool() {
		return Math.random() < 0.5; // Random boolean generator.
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

	/*
	 * Adds a new job to the workQueue based on random parameters. Either a trailer
	 * job or a mansion job.
	 */
	static void newJob() {
		final int TRAILER_CASE = 0;
		final int MANSION_CASE = 1;

		int trailerOrMansion = (int) (Math.random() * 10) % 2;

		switch (trailerOrMansion) {
		case TRAILER_CASE:
			addTrailerJob();
			break;

		case MANSION_CASE:
			addMansionJob();
			break;

		default:
			System.out.println("Error!");
		}
	}

	/*
	 * Creates a job set in a for loop based on the random number generated in the
	 * main() function.
	 */
	private static void createNewJobSet() {
		// Initialize the first set of jobs
		int numJobs = generateRanNumber(1, 2);

		for (int i = 0; i < numJobs; i++) {
			newJob();
		}
	}

	/*
	 * If a trailer job is added, this method generates the random number of rooms
	 * and then calculates the number of wheels to find the value of the trailer.
	 */
	private static void addTrailerJob() {
		int tempRooms = generateRanNumber(minTrailerRooms, maxTrailerRooms);
		int tempWheels = tempRooms * NumOfWheelsPerRoom;
		workQueue.add(new Trailer(tempWheels, tempRooms));
		jobCounter++;
		System.out.println("\nGot a new Job building a " + tempWheels + " wheeled, " + tempRooms + " room Trailer.");
	}

	/*
	 * If a mansion job is added, this method generates the random number of floors
	 * and then calculates the number of rooms to find the value of the mansion.
	 */
	private static void addMansionJob() {
		int tempFloors = generateRanNumber(minMansionFloors, maxMansionFloors);
		int tempRooms = generateRanNumber(minNumberOfRoomsPerFloor, maxNumberOfRoomsPerFloor) * tempFloors;
		workQueue.add(new Mansion(tempRooms, tempFloors, randomBool(), randomBool()));
		jobCounter++;
		System.out.println("\nGot a new Job building a " + tempFloors + " floored, " + tempRooms + " room Mansion.");
	}

	// Checks if there is any pending work.
	boolean workLeft() {
		return !workQueue.isEmpty();
	}

	// Completes the current job and updates the wallet.
	static void doJob() {
		try {
			if (workQueue.peek().doWork()) {
				wallet += workQueue.peek().value;
				workQueue.remove();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Main method which generates random number of jobs and checks if money goal is
	 * reached. Returns print statement with number of total jobs
	 */
	public static void main(String args[]) {
		HouseBuilder houseBuilder = new HouseBuilder();

		// Keeps adding and completing jobs in batches until the revenue goal is reached.
		while (wallet < revenueGoal) {

			createNewJobSet();
			
			while (houseBuilder.workLeft()) {
				doJob();
			}
		}

		System.out.println(houseBuilder.summarizeWork());
	}
}
