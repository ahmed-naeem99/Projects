/*
 * Name: Ahmed Naeem
 * Date: Jan 20, 2024
 * Description: This program represents a generic construction management system using abstract classes.
 *              By introducing abstract classes for different house types, the program becomes more versatile.
 *              Electrician file manages construction jobs, including both generic houses and specific types like Mansion and Trailer.
 * 
 * Input: The program doesn't take user input in this version. It generates construction jobs randomly.
 * Output: The program outputs the progress of construction jobs and the total earnings after completing all jobs.
 */

package abstractJavaHouse;

/*
 * Mansion is a type of house with 1 or more floors, with/without a driveway, and may be furnished
 */
class Mansion extends abstractHouse {
	int numOfFloors;
	boolean hasDriveway;
	boolean isFurnished;

	Mansion(int rooms, int floors, boolean hasDriveway, boolean isFurnished) {
		numOfRooms = rooms;
		numOfFloors = floors;
		this.hasDriveway = hasDriveway;
		this.isFurnished = isFurnished;
		workCounter = 0;
		workRequired = 10;
		calculateValue(); //To-do
	}

	//This method in mansion class calculates value based on number of floors, existence of driveway, and whether it is furnished or not. 
	@Override
	void calculateValue() {

		final double ROOM_VALUE = 2000;
		final double FLOOR_VALUE = 1000;
		final double DRIVEWAY_VALUE = 200;
		final double FURNITURE_VALUE = 500;

		value = (numOfRooms * ROOM_VALUE) + (numOfFloors * FLOOR_VALUE);
		
		if (hasDriveway) {
			value += DRIVEWAY_VALUE;
			// Additional work required for a driveway.
			workRequired += 20;
		}
		if (isFurnished) {
			value += FURNITURE_VALUE;
			// Additional work required for furnishing.
			workRequired += 100;
		}
	}

	@Override
	String getHouseType() {
		return "Mansion";
	}
	
}
