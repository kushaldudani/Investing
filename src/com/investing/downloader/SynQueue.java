package com.investing.downloader;
import java.util.LinkedList;


public class SynQueue<E> {
	
	private LinkedList<E> queue = new LinkedList<E>();
	
	public SynQueue() {
	}
	
	public synchronized void enqueue(E entry){
		queue.add(entry);
	}
	
	public synchronized E dequeue(){
		return queue.poll();
	}
	

}