package src.com.qa.executioner;

import src.com.qa.driver.BaseDriver;

public class Executioner {
	//Main method Excecution Starts from Here
	public static void main(String[] args) {
		BaseDriver baseDriver = new BaseDriver();
		baseDriver.init();
		baseDriver.logIn();
		baseDriver.validateAccId();
		baseDriver.clearShippingCart();
		baseDriver.bookDetails();


	
	}

}
