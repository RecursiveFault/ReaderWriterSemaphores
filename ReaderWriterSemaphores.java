/*
 * ReadWriteDriver.java
 * Matthew Moellman
 * CSC460
 * 6 April 2016
 */
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

/*
 * I know that this data could've been in the main driver and passed differently, but I thought this more modular, OO approach made more sense 
 * for a real application, where readers/writers that don't share files could share a different set of semaphores.
 */

public class ReaderWriterSemaphores {
	
	public int readcount =0, writecount =0; //init int variables
	public Semaphore readMutex = new Semaphore(1,true); //Semaphore initialized as binary semaphores, no fairness settings
	public Semaphore writeMutex = new Semaphore(1,true);
	public Semaphore writeQueue = new Semaphore(1); //writers wait to get in
	public Semaphore writerWait = new Semaphore(1); //readers will block if writers are waiting
	
	public File file1 = new File("1.txt"); //declare the files as new files
	public File file2 = new File("2.txt");
	public File file3 = new File("3.txt");
	public File file4 = new File("4.txt");

	
	public int totalreads=0,totalwrites=0; //counters to compare total numbers of reads and writes
	
	ReaderWriterSemaphores(){} //empty constructor
	
}
