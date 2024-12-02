package abstractJavaHouse;

public class Trailer extends abstractHouse{

	int numOfWheels;
	
	/*
	 * calculateValue calculates the value of the trailer based on the number of rooms.
	 */
	@Override
	void calculateValue() {

		//Trailer value was CHANGED because first 
		value = numOfWheels * numOfRooms * 1000;
		
	}
	
	/*
	 * Default constructor, a trailer is pretty consistent work hence always a workRequired value of 10
	 */
	Trailer(int wheels, int rooms){
		
		numOfRooms = rooms;
		numOfWheels = wheels;
		workCounter = 0;
		workRequired = 10;
		calculateValue();

	}
	
	String getHouseType() {
		return "Trailer";
	}

}
