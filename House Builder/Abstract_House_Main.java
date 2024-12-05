package abstractJavaHouse;

import java.util.LinkedList;
import java.util.Queue;

public class Abstract_House_Main {
	
	public static void main(String args[]) {

		Queue<abstractWorker> workers = new LinkedList<>();
		
		workers.add(new Electrician());
		workers.add(new Contractor2());
		
		while (!workers.isEmpty()) {
			abstractWorker worker = workers.poll(); 
			
			worker.createNewJobSet(); 

			while (worker.workLeft()) {

				worker.doJob(); // work on the current job

				if (worker.advertiseContract()) { // if successful in advertising a new job

					worker.createNewJobSet(); // add a new job to the queue
				}
			}
			System.out.println(worker.summarizeWork());
		}
	}
	
	
}
