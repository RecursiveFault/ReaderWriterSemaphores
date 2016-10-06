/*
 * ReadWriteDriver.java
 * Matthew Moellman
 * CSC460
 * 6 April 2016
 */

/*
 * I decided to model the Readers/Writer problem with 4 separate files that were randomly chosen by both the reader and writer threads.
 * Each writer outputs to the console after writing, and each reader reads to the console as its read action.
 * Because I used actual files, if this is run multiple times in succession, after the first run the files will have *already been created*, and thus will already have Writer strings at the start of execution.
 */

public class ReadWriteDriver {
	
	public static void main(String[] args) throws InterruptedException{
		
		ReaderWriterSemaphores rws = new ReaderWriterSemaphores(); //init the Semaphore class, which inits the files, semaphores, and counts of total reads/writes-- to be passed
		
	
		Writer w1 = new Writer("1st writer.",rws); //Writers are instantiated with the string to be written, and the semaphore class reference
		Writer w2 = new Writer("2nd writer!",rws);
		Writer w3 = new Writer("3rd writer?",rws);
		
		Reader r1 = new Reader(rws); //Readers only are passed the semaphore class reference
		Reader r2 = new Reader(rws);
		Reader r3 = new Reader(rws);
		
		
		new Thread(w1).start(); //start first reader and writer threads
		Thread.sleep(20); //stagger start time of the initial reader and writer somewhat, helps prevent reader from trying to do reading before anything is written
		new Thread(r1).start();
		Thread.sleep(10000); //sleep for 10 secs, lets the first two run for awhile just to show the output
		
		new Thread(w2).start(); //start 2 more writers
		new Thread(w3).start();
		new Thread(r2).start(); //another 2 readers
		new Thread(r3).start();
		Thread.sleep(60000); //let threads run for 60 secs
		
		System.out.println("Done!");
		System.out.println("Total number of reads with: " + rws.totalreads + " Total number of writes: " + rws.totalwrites); //Print the counted number of reads and writes that took place.
		System.exit(0); //forcibly closes program after time limit
	}

}
