package no.hvl.dat110.transport.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import no.hvl.dat110.application.ReceiverProcess;
import no.hvl.dat110.application.SenderProcess;
import no.hvl.dat110.network.Network;
import no.hvl.dat110.transport.TransportReceiver;
import no.hvl.dat110.transport.TransportSender;
import no.hvl.dat110.network.IAdversary;

public class TestTransport {

	private Network network;
	private TransportSender tsender;
	private TransportReceiver treceiver;
	
	ArrayList<byte[]> datasent,datarecv;
	
	private int RUNNINGTIME = 10000;
	
	public void setRunningTime(int msecs) {
		this.RUNNINGTIME = msecs;
	}
	
	public void setupNetwork(IAdversary adversary) {
		
		this.network = new Network(adversary);
	}
	
	public void setupTransport(TransportSender tsender, TransportReceiver treceiver) {
		
		this.tsender = tsender;
		this.treceiver = treceiver;
		
		tsender.register(network.getService(0));
		treceiver.register(network.getService(1));
	}
	
	public void runTest() {
		
		network.doRun();
			
		tsender.start();
		treceiver.start();
		
		SenderProcess sender = new SenderProcess(tsender);
		ReceiverProcess receiver = new ReceiverProcess(treceiver);
		
		sender.doRun();
		
		try {
			
			Thread.sleep(RUNNINGTIME); // allow for reception of outstanding messages
			
			tsender.doStop();
			
			tsender.join();
			
			treceiver.doStop();
			
			treceiver.join();
			
			network.doStop();
			
			
		} catch (InterruptedException ex) {

			System.out.println("Main thread " + ex.getMessage());
			ex.printStackTrace();
		}
		
		datasent = sender.getDatasent();
		datarecv = receiver.getDatarecv();
		
	}
	
	public ArrayList<byte[]> getDataReceived() {
		return datarecv;
		
	}
	
	public ArrayList<byte[]> getDataSent() {
		return datasent;
		
	}
	
	public boolean equalData(byte[] data1, byte[] data2) {
		
		return Arrays.equals(data1, data2);
	}
	
	public void assertRDT() {
		
		assertEquals(datasent.size(),datarecv.size());
		
		for (int i = 0; i<datasent.size();i++) {
			assertTrue(equalData(datasent.get(i),datarecv.get(i)));
		}
	}
}
