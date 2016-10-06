/*
 * Writer.java
 * Matthew Moellman
 * CSC460
 * 6 April 2016
 */
import java.io.*;
import java.util.Random;

public class Writer implements Runnable{
	
	private ReaderWriterSemaphores rws;
	String towrite;
	
	public Writer(String s, ReaderWriterSemaphores r){
		System.out.println("Writer created!"); //Verbose output for debug
		towrite=s; //string passed to constructor, to be written by this thread
		rws=r; //hold semaphore class reference
	}
	
	@Override
	public void run(){
		do{ 
			try {
			rws.writeMutex.acquire();
			rws.writecount++;
			rws.writeMutex.release();
			
			if (rws.writecount > 1 || rws.readcount > 0)
				rws.writeQueue.acquire();
			
			} catch (InterruptedException e) {}
			
			
			//Critical section begins
			Random r = new Random();
			int file = r.nextInt(4)+1; //get random int, 1-4
			
			try{ //catch file IO exceptions
			
			switch(file){ //switch based on random gen int
				case 1:  //File 1
					PrintWriter pWriter1 = new PrintWriter((new FileWriter(rws.file1)));
					pWriter1.write(towrite); //overwrite file's content with the new tostring
					System.out.println("I wrote " + towrite + " to File 1"); //Print what action was taken to show verbose debug
					pWriter1.close();
					break;
				case 2:
					PrintWriter pWriter2 = new PrintWriter((new FileWriter(rws.file2)));
					pWriter2.write(towrite);
					System.out.println("I wrote " + towrite + " to File 2");
					pWriter2.close();
					break;
				case 3:
					PrintWriter pWriter3 = new PrintWriter((new FileWriter(rws.file3)));
					pWriter3.println(towrite);
					System.out.println("I wrote " + towrite + " to File 3");
					pWriter3.close();
					break;
				case 4:
					PrintWriter pWriter4 = new PrintWriter((new FileWriter(rws.file4)));
					pWriter4.println(towrite);
					System.out.println("I wrote " + towrite + " to File 4");
					pWriter4.close();
					break;
				default: //Error in random number
					System.out.println("File number passed was NOT 1-4"); //error output if switch value is not 1-4
					break;
			}
			} catch (IOException e1) {}
			
			try{ //have to surround Thread and acquire() calls with try/catch
			rws.writeMutex.acquire();
			rws.writecount--;
			rws.totalwrites++;
			if (rws.writecount >0)
				rws.writeQueue.acquire();
			else{
				rws.readMutex.acquire();
				while(rws.readcount>0)
				{
					rws.writerWait.release();
					rws.readcount--;
				}
			}
			rws.writeMutex.release(); 
			rws.readMutex.release();
			
			Thread.sleep(1000);//This sleep is used to force the writers to write less often than readers read. It more accurately portrays the type of 
							  //access pattern they would share, with reads being generally far more common than writes. It also helps the chance of starvation by 
							 //staggering attempts to access the writeMutex and writeQueue
			
			} catch (InterruptedException e) {}
			
		}while(true); //loop forever
	}
	
}
