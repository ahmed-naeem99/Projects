package abstractJavaHouse;

public class Contractor2 extends abstractWorker{

	@Override
	boolean advertiseContract() {
		
		if (workQueue.size() < 3 && (int)Math.floor((Math.random() * 100) % 7) == 0){
			
			return true;
		}
		return false;
	}
	
	@Override
	String getWorkerType() {
		return "Contractor";
	}

/*
public static void main(String args[]){
		
		Contractor2 myContractor = new Contractor2();
		
		myContractor.newJob();	//initial job for the contractor
		
		while (myContractor.workLeft()){
			
			myContractor.doJob();//work on the current job
			
			if (myContractor.advertiseContract()){	//if successful in advertising a new job
				
				myContractor.newJob();	//add a new job to the queue
			}
			
		}
		
		System.out.println(myContractor.summarizeWork());
		
		
	}
*/
}
