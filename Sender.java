/*
 * Name: Alfath Zikir
 * UCID: 30027320
 * Assignment 3
 * Class: Sender
 **/


import java.io.*;
import java.net.*;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class Sender{

    private static ArrayList<Object> objectList = new ArrayList<>();
	private static int choice;
	private static Scanner input;
	
	private static final String fileName = "sentData.xml";
	private static final String SERVER = "localhost";
	private static int PORT;

	public static void main(String[] args) throws IOException {
		
		if (args.length != 1) {
			System.out.println("Program failed to start. Need to input PORT number.");
			System.exit(1);
		}
		
		PORT = Integer.parseInt(args[0]);
		generateMenu(PORT);
		
	}
	
	// Generate Menu for User Prompt
	private static void generateMenu(int PORT) throws IOException {
		
		boolean end = false;
		
		while (!end) {
			
			System.out.println("=======================================================================");
			System.out.println("Types of Object:\n"
					+ "1. A simple object with only primitives.\n"
					+ "2. An object that contains references to other objects.\n"
					+ "3. An Object that contains an array of primitives.\n"
					+ "4. An object that contains an array of object references.\n"
					+ "5. An object that uses Java's Collection class.\n"
					+ "6. Display objects created.\n"
					+ "7. Serialize and send object list to the receiver.\n"
					+ "0. Terminate program.");
			System.out.println("=======================================================================\n");
			
			System.out.println("Choose a valid option:");             
			input = new Scanner(System.in);
			
			while(!input.hasNextInt()){
		            input.next();
		            System.out.println("Enter a valid integer:");
		        }
						
			choice = input.nextInt();  
			switch(choice){
				case 0:
					System.out.println("Terminating program...");
					System.exit(0);
				case 1:
					objectList.add(ObjectCreator.createSimpleObject());
					break;
	    		
	    		case 2:
	    			objectList.add(ObjectCreator.createReferenceObject());
	    			break;
	    		
	    		case 3:
	    			objectList.add(ObjectCreator.createPrimitiveArrayObject());
	    			break;
	    		
	    		case 4:
	    			objectList.add(ObjectCreator.createReferenceArrayObject());
	    			break;
	    		
	    		case 5:
	    			objectList.add(ObjectCreator.createCollectionObject());
	    			break;
	    		
	    		case 6: 
	    			System.out.println(objectList.toString() + "\n");
	    			break;
	    			
	    		case 7:
	    			serialize(SERVER, PORT);
	    			break;
	    			
	    		default:
	    			System.out.println("Invalid Input!");
	    			break;
			}
		}
	}
	
	// Start serializing
	private static void serialize(String server, int port) throws IOException {

		System.out.println("Serialize and send object list to the receiver?\n"
				+ "1. Yes\n"
				+ "2. No");

		input = new Scanner(System.in);
		choice = input.nextInt();
		
		if (choice == 1) {
			
			for (Object object : objectList) {
				
				System.out.println("Deserializing Object...");
				
				Document doc = Serializer.serialize(object);
				
				File file = createXML(doc);
				
				sendFile(server, port, file);
				
			}
			
		}else if (choice == 2){
			
		}else {
			System.out.println("Invalid input!");
		}
		
	}

	// Send file to the receiver
	private static void sendFile(String server, int port, File file){
		
		System.out.println("Sending file to " + server + " on port: " + port);
		
		try {
			
			// Create Socket
			Socket socket = new Socket (server, port);
			
			// IO Streams
			OutputStream out = socket.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(file);
		
			// Send file as bytes
			byte[] bytes = new byte[1024 * 1024];
			int bytesRead = 0;
			while ((bytesRead = fileInputStream.read(bytes)) > 0) {
				out.write(bytes, 0, bytesRead);
			}
			
			// Close everything
			out.close();
			fileInputStream.close();
			socket.close();
			
			System.out.println("Transfer Complete");
		
		}catch (IOException e){
			System.out.println("Failed Connection...");
		}

	}

	
	// Create an XML file of serialized object
	private static File createXML(Document doc) throws IOException {

		File file = new File(fileName);
		
		XMLOutputter out = new XMLOutputter();
		out.setFormat(Format.getPrettyFormat());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		out.output(doc, writer);
		
		writer.close();
			
		return file;
	}

}