/*
 * Name: Ahmed Naeem
 * Date: Jan 20, 2024
 * Description: This program represents a generic construction management system using abstract classes.
 *              By introducing abstract classes for different house types, the program becomes more versatile.
 *              The main file, Electrician, manages construction jobs, including both generic houses and specific types like Mansion and Trailer.
 * 
 * Input: The program doesn't take user input in this version. It generates construction jobs randomly.
 * Output: The program outputs the progress of construction jobs and the total earnings after completing all jobs.
 */

package abstractJavaHouse;

/*
 * AbstractHouse represents a generic house.
 * Serves as an abstract class for any type of house.
 */
abstract class abstractHouse {
	int numOfRooms;
	double value;
	int workRequired;
	int workCounter;

	//Every child class should implement calculateValue method based on it's own parameters.
	abstract void calculateValue();

	abstract String getHouseType();
		
	//Does not need to be abstract since it is not class-specific  
	boolean doWork() {
			workCounter++;
		
		if (workCounter < workRequired){
			return false;
		}
		else {
			System.out.println("Done working on a " + getHouseType() + " job.");
			return true;
			
		}
		
	}
}
