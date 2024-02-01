//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
//
// Class          : vendingMachine.Model
//
// Author         : Richard E. Pattis
//                  Computer Science Department
//                  Carnegie Mellon University
//                  5000 Forbes Avenue
//                  Pittsburgh, PA 15213-3891
//                  e-mail: pattis@cs.cmu.edu
//
// Maintainer     : Author
//
//
// Description:
//
//   The Model for the VendingMachine package implements the guts of the
// vending machine: it responds to presses of buttons created by the
// Conroller (deposit, cancel, buy), and tells the View when it needs
// to update its display (calling the update in view, which calls the
// accessor methods in this classes)
// 
//   Note that "no access modifier" means that the method is package
// friendly: this means the member is public to all other classes in
// the calculator package, but private elsewhere.
//
// Future Plans   : More Comments
//                  Increase price as stock goes down
//                  Decrease price if being outsold by competition
//                  Allow option to purchase even if full change cannot 
//                    be returned (purchaser pays a premium to quench thirst)
//                  Allow user to enter 2 x money and gamble: 1/2 time
//                    all money returned with product; 1/2 time no money and
//                    no product returned
//
// Program History:
//   9/20/01: R. Pattis - Operational for 15-100
//   2/10/02: R. Pattis - Fixed Bug in change-making method
//
//
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////


package vendingMachine;


import java.lang.Math;
import java.util.Scanner;


public class Model {
	//Define fields (all instance variables)
	
	private View view;         // Model must tell View when to update itself
	
	private int    cokeLeft;
	private int    pepsiLeft;
	
	private int    quartersLeft, dimesLeft, nickelsLeft;
	private int cokePrice, pepsiPrice;
	private int quartersDeposited, dimesDeposited, nickelsDeposited;
	private int centsDeposited;
	private String message;
	
	//I defined about 10 more fields
	
	//Define constructor
	public Model(){
		quartersLeft = getUserInt("Enter number of starting quarters:");
		dimesLeft = getUserInt("Enter number of starting dimes:");
		nickelsLeft = getUserInt("Enter number of starting nickels:");
		cokeLeft = getUserInt("Enter number of Coke bottles:");
		pepsiLeft = getUserInt("Enter number of Pepsi bottles:");
		cokePrice = getUserInt("Enter price of a Coke bottle (in cents):");
		pepsiPrice = getUserInt("Enter price of a Pepsi bottle (in cents):");
		quartersDeposited = 0;
		dimesDeposited = 0;
		nickelsDeposited = 0;
		centsDeposited = 0;

	}
	private int getUserInt(String msg){
		System.out.println(msg);
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		while(!isPositiveInt(s)){
			System.out.println("Please enter a positive integer!");
			s = sc.nextLine();

		}
		int i = Integer.parseInt(s);
		return i;

	}
	private boolean isPositiveInt(String s){
		int i;
		try{
			i = Integer.parseInt(s);
		}
		catch (Exception e){
			return false;
		}
		if(i > 0){
			return true;

		}
		return false;
	}

	//Refer to the view (used to call update after each button press)
	public void addView(View v)
	{view = v;}
	
	//Define required methods: mutators (setters) and accessors (getters)
	
	//Represent "interesting" state of vending machine
	public String toString()
	{
		return "Vending Machine State: \n" +
			"  Coke     Left      = " + cokeLeft     + "\n" +
			"  Pepsi    Left      = " + pepsiLeft    + "\n" +
			"  Quarters Left      = " + quartersLeft + "\n" +
			"  Dimes    Left      = " + dimesLeft    + "\n" +
			"  Nickels  Left      = " + nickelsLeft  + "\n";
		//Display any other instance variables that you declare too
	}
	
	//Define helper methods

	public void cancel(){
		quartersLeft += quartersDeposited;
		dimesLeft += dimesDeposited;
		nickelsLeft += nickelsDeposited;
		quartersDeposited = 0;
		dimesDeposited = 0;
		nickelsDeposited = 0;
		centsDeposited = 0;
		message = "Deposit cancelled, returning coins...";
		view.update();



	}
	public void deposit(int amount){
		if(amount == 25){
			if(quartersLeft == 0){
				message = "You don't have anymore quarters!";

			}
			else{
				quartersLeft--;
				quartersDeposited++;
				message = "Deposited a quarter!";
				centsDeposited += amount;
			}
		}
		else if(amount == 10){
			if(dimesLeft == 0){
				message = "You don't have anymore dimes!";
			}
			else {
				dimesLeft--;
				dimesDeposited++;
				message = "Deposited a dime!";
				centsDeposited += amount;
			}
		}
		else{
			if(nickelsLeft == 0){
				message = "You don't have any more nickels!";
			}
			else {
				nickelsLeft--;
				nickelsDeposited++;
				message = "Deposited a nickel!";
				centsDeposited += amount;
			}
		}
		message = message + " Quarters: " + quartersDeposited + " Dimes: " + dimesDeposited + " Nickels: " + nickelsDeposited;
		double d = (double )centsDeposited / 100.0;
		message = message + " Amount Deposited $" + d;
		view.update();

	}
	public void buy(String product){
		int price;
		int change;
		int quarter = 0;
		int dime = 0;
		int nickel = 0;
		if(product.equals("Coke")){
			if(cokeLeft == 0){
				message = "No more coke left!";
				view.update();
				return;
			}
			price = cokePrice;
		}
		else{
			if(pepsiLeft == 0){
				message = "No more pepsi left!";
				view.update();
				return;
			}
			price = pepsiPrice;
		}
		System.out.println(centsDeposited + ", " + price);
		if(centsDeposited < price){
			message = "This purchase is not allowed! You only deposited $" + centsDeposited/100.0 + "!";
			view.update();
			return;
		}
		else{
			change = centsDeposited - price;
			while(change != 0){
				if(change - 25 >= 0){
					change -= 25;
					quartersLeft++;
					quarter++;
				}
				else if(change - 10 >= 0){
					change -= 10;
					dimesLeft++;
					dime++;
				}
				else{
					change -= 5;
					nickelsLeft++;
					nickel++;
				}
			}
			if(product.equals("Coke")){
				message = "Bought 1 Coke bottle!";
				cokeLeft--;
			}
			else{
				message = "Bought 1 Pepsi bottle!";
				pepsiLeft--;
			}
		}
		dimesDeposited = 0;
		quartersDeposited = 0;
		nickelsDeposited = 0;
		centsDeposited = 0;
		message += " Change: " + quarter + " quarters, " + dime + " dimes, " + nickel + " nickels";

		view.update();

	}
	public String getDeposited(){
		return quartersLeft + ", " + dimesLeft + ", " + nickelsLeft;
	}
	public String getMessage(){
		return message;
	}
	public int getCokeLeft(){
		return cokeLeft;
	}
	public int getPepsiLeft(){
		return pepsiLeft;
	}
	public String getPepsiPrice(){
		double d = (double) pepsiPrice / 100;
		return "$" + d;
	}
	public String getCokePrice(){
		double d = (double) cokePrice / 100;
		return "$" + d;
	}


}
