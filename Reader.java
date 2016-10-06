/*
 * Reader.java
 * Matthew Moellman
 * CSC460
 * 6 April 2016
 */
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Reader implements Runnable {
	
	private ReaderWriterSemaphores rws; //hold value
	
	public Reader(ReaderWriterSemaphores r){
		System.out.println("Reader created!");
		rws=r;
	}
	
	public void run (){
		do {
			try{
			rws.readMutex.acquire();
			rws.readcount++;
			rws.readMutex.release();
			
			rws.writeMutex.acquire();
			if (rws.writecount > 0)
			{
				rws.writeMutex.release();
				rws.writerWait.acquire();
			}
			else
			{
				rws.writeMutex.release();
			}
			} catch (InterruptedException e) {}
			
			
			//Critical section begins
			String s;
			try{ //This try/catch block reads every file that has a nextline
					Scanner sc1 = new Scanner(rws.file1); 
					if(sc1.hasNextLine())
					{
						while (sc1.hasNextLine())
						{
							s = sc1.nextLine();
							System.out.println("   File 1: "+s);
						}
					}
					Scanner sc2 = new Scanner(rws.file2);
					if(sc2.hasNextLine())
					{
						while (sc2.hasNextLine())
						{
							s = sc2.nextLine();
							System.out.println("   File 2: "+s);
						}
					}
					Scanner sc3 = new Scanner(rws.file3);
					if(sc3.hasNextLine())
					{
						while (sc3.hasNextLine())
						{
							s = sc3.nextLine();
							System.out.println("   File 3: "+s);
						}
					}
					Scanner sc4 = new Scanner(rws.file4);
					if(sc4.hasNextLine())
					{
						while (sc4.hasNextLine())
						{
							s = sc4.nextLine();
							System.out.println("   File 4: "+s);
						}
					}
			}catch(FileNotFoundException e1){}
			
			rws.readMutex.release(); //no acquires, so no try/catch needed here
			rws.writeMutex.release();
			rws.readcount--;
			rws.totalreads++; //increase total counter
			if ((rws.readcount==0)&&(rws.writecount>0))
			{
				rws.writeQueue.release();
				rws.readMutex.release();
				rws.writeMutex.release();
			}
			else
			{
				rws.readMutex.release();
				rws.writeMutex.release();
			}
			try {
				Thread.sleep(600); //slows down reads, just to make the outputs more readable
			} catch (InterruptedException e) {}
			
		}while(true);
	}
}
