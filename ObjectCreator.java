/*
 * Name: Alfath Zikir
 * UCID: 30027320
 * Assignment 3
 * Class: ObjectCreator
 **/


import java.util.*;
import java.io.*;

public class ObjectCreator {

	private static Scanner input;
	// Creates an instance of CollectionObject class
	static CollectionObject createCollectionObject() {

		System.out.println("Creating an object that uses Java's Collection class.\n");
		
	    CollectionObject collectionObject = null;
		ArrayList<SimpleObject> collectionList = new ArrayList<>();
		input = new Scanner(System.in);
		boolean end = false;
		
		while (!end) {
			
			System.out.println("Do you want to enter an object to the collection?\n"
            		+ "1. Yes\n"
            		+ "2. No\n");
			
            while(!input.hasNextInt()){
                input.next();
                System.out.println("Enter 1 or 2!\n");
            }
            
            int choice = input.nextInt();
            
            if (choice == 1) {
            	
            	SimpleObject simpleObject = createSimpleObject();
            	collectionList.add(simpleObject);
          
            	
            }else if (choice == 2) {
            	
            	collectionObject = new CollectionObject(collectionList);
            	end = true;
            	
            }else {
            	System.out.println("Enter 1 or 2!");
            }
            
		}
		
		System.out.println("You have created an object that uses Java's Collection class!");
		
		return collectionObject;
	}

	// Creates an instance of ReferenceArrayObject class
	static ReferenceArrayObject createReferenceArrayObject() {

		System.out.println("Creating an object containing an array of object references.\n");
		System.out.println("Enter array length: \n");
		
		input = new Scanner(System.in);
		
		int length = input.nextInt();
		
		SimpleObject[] simpleObjectArray = new SimpleObject[length];
		
		// Initialize array with SimpleObjects
		for (int i = 0; i < length; i++) {
			SimpleObject simpleObj = createSimpleObject();
			simpleObjectArray[i] = simpleObj;
		}
		
		ReferenceArrayObject referenceArrayObject = new ReferenceArrayObject(simpleObjectArray);
		System.out.println("You have created an object containing an array of object references!");
		
		
		return referenceArrayObject;
	}

	// Creates an instance of PrimitiveArrayObject class
	static PrimitiveArrayObject createPrimitiveArrayObject() throws IOException {

		System.out.println("Creating an object containing an array of primitives.\n");
		System.out.println("Enter values for array separated by commas. Example: '1,2'.\n");
		
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String input = bufferRead.readLine();
		String[] values = input.split(",");
		
		int[] intArray = new int[values.length];
		for (int i = 0; i < intArray.length; i++) {
			try {
				intArray[i] = Integer.parseInt(values[i]);
			} catch (Exception e) {
				System.out.println("Please enter only integers!");
			}
		}
		
		PrimitiveArrayObject primitiveArrayObject = new PrimitiveArrayObject(intArray);
		
		System.out.println("You have created an object containing an array of primitives!\n");
		
		return primitiveArrayObject;
	}

	// Creates an instance of ReferenceObject class
	static ReferenceObject createReferenceObject() {

		System.out.println("Creating a referenced Object.\n");
		
		SimpleObject simpleObject = createSimpleObject();
		ReferenceObject referenceObject = new ReferenceObject(simpleObject);
		
		System.out.println("You have created a Reference Object!\n");
		
		return referenceObject;
	}

	
	// Creates an instance of SimpleObject class
	static SimpleObject createSimpleObject() {

		SimpleObject simpleObject = null;
		System.out.println("Creating a simple object with primitive values.\n");
		System.out.println("Enter Field Values: (int, float)");
		
		try {
			
			input = new Scanner(System.in);

            System.out.println("Enter an integer for the 1st field.\n ");
            
            //Checks if the user inputs an integer
            while(!input.hasNextInt()){
                input.next();
                System.out.println("Enter a valid Integer for the 1st field!");
            }
            
            int intField = input.nextInt();
 
            System.out.println("Enter a float for the 2nd field.\n ");
            
            //Checks if the user inputs a float
            while(!input.hasNextFloat()){
                input.next();
                System.out.println("Enter a valid float for the 2nd field!");
            }
            
            float floatField = input.nextFloat();

            simpleObject = new SimpleObject(intField, floatField);
            System.out.println("You have created a Simple Object!\n");

		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return simpleObject;
	}
	
}
