package abstractJavaHouse;

public class Electrician extends abstractWorker {

	@Override
	boolean advertiseContract() {
		if (wallet <= revenueGoal) {
			return true;
		}
		return false;
	}
	
	@Override
	void doJob() {		
		while (!(workQueue.peek().doWork())) {
			// Keep doing work until the job is done
		}
		
        wallet += workQueue.peek().value;
        workQueue.remove(); 
    }
	
	@Override
	String getWorkerType() {
		return "Electrician";
	}
	
/*
	public static void main(String args[]) {

		Electrician myElectrician = new Electrician();

		myElectrician.createNewJobSet(); // initial job for the electrician

		while (myElectrician.workLeft()) {

			myElectrician.doJob();// work on the current job

			if (myElectrician.advertiseContract()) { // if successful in advertising a new job

				myElectrician.createNewJobSet(); // add a new job to the queue
			}
		}
		System.out.println(myElectrician.summarizeWork());

	}
*/
}
