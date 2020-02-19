package no.hvl.dat110.network.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import no.hvl.dat110.network.Channel;
import no.hvl.dat110.network.models.RDT1PerfectChannel;
import no.hvl.dat110.network.*;
import no.hvl.dat110.transport.*;

public class TestChannel {

	@Test
	public void test() {

		Channel channel = new Channel("Test channel", new RDT1PerfectChannel());
		
		byte[] data = { 1, 2, 3, 4 };
		
		Datagram datasend = new Datagram (new Segment(data));

		channel.send(datasend);

		Datagram datarecv = channel.receive();

		assertEquals(datasend, datarecv);

	}

}
