package com.laneve.test;

import java.util.Random;

public class DiningPhilosopher extends Thread{

	
	private int number;
	private Fork left, right;
	private static Random random = new Random();
	
	public DiningPhilosopher(int number, Fork left, Fork right){
		this.number = number;
		this.left = left;
		this.right = right;
	}
	
	public String toString(){
		return "Fil no. " + number;
	}
	
	private void think(){
		System.out.println(this.toString() + " is thinking");
		
		try{
			Thread.sleep((long)random.nextFloat() * 1000);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	private void eat(){
		System.out.println(this + " takes left fork no. " + left.getNumber());
		synchronized(left){
			System.out.println(this + " takes right fork no. " + right.getNumber());
			synchronized(right){
				System.out.println(this + " is eating");
				try{
					Thread.sleep((long)random.nextFloat() * 1000);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void run(){
		think();
		eat();
		this.run();
	}

	public static void main(String[] args) {

		Fork f0= new Fork(0);
		Fork f1= new Fork(1);
		Fork f2= new Fork(2);
		
		DiningPhilosopher d1 = new DiningPhilosopher(0,f0,f2); //Deadlock
		//DiningPhilosopher d1 = new DiningPhilosopher(0,f2,f0); //No Deadlock
		DiningPhilosopher d2 = new DiningPhilosopher(1,f1,f0); 
		DiningPhilosopher d3 = new DiningPhilosopher(2,f2,f1); 
		
		d1.start();
		d2.start();
		d3.start();

	}

}

class Fork{
	
	private int number;
	
	public Fork(int number){
		this.number = number;
	}
	
	public int getNumber(){
		return number;
	}
}
