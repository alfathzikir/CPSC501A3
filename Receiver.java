/*
 * Name: Alfath Zikir
 * UCID: 30027320
 * Assignment 3
 * Class: Receiver
 **/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Receiver extends Thread {

	private static int PORT;
	private static final String fileName = "receivedData.xml";
	
	private static ServerSocket serverSocket;
	private static Socket socket;
	

	
	public static void main (String[] args) {
		
		
		if (args.length != 1) {
			System.out.println("Program failed to start. Need to input PORT number.");
			System.exit(1);
		}
		
		PORT = Integer.parseInt(args[0]);
		
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
		    serverSocket.setSoTimeout(200000);
		} catch (IOException e) {

			System.err.println("Port " + PORT + " failed.");
		}
		while (true) {
			
			System.out.println("Server running on " + serverSocket.getLocalPort());
			File file = new File(fileName);
			
			try {
				socket = serverSocket.accept();
				System.out.println("Connected with client");
				
				receiveFile(file, socket);
				Object object = buildObjectFromFile(file);
				
				// Visualize object
				System.out.println("=======================================================================");
				Inspector inspector = new Inspector();
				inspector.inspect(object, false);
				System.out.println("=======================================================================\n");
				
			}catch (IOException e) {
				System.err.println("Connection failed.");
			}
				
		}
	

	}
	
	// A method that builds an object from a file by deserializing XML document with SAXBuilder
	private static Object buildObjectFromFile(File file) {
		
		SAXBuilder saxBuilder = new SAXBuilder();
		Object object = null;
		
		try {
			
			Document doc = (Document) saxBuilder.build(file);
			object = Deserializer.deserialize(doc);
			
		} catch (JDOMException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}

	// A method that receives the file as bytes array
	public static void receiveFile(File file, Socket socket) {
		
		try {
			
			//IO Stream
			InputStream inputStream = socket.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(file);

			// Receive File as byte array
			byte[] bytes = new byte[1024 * 1024];
			int bytesRead = 0;
			while((bytesRead =  inputStream.read(bytes)) > 0){
				fileOutputStream.write(bytes, 0, bytesRead);
				break;
			}
			
        }catch(Exception e){
        	System.err.println("IO Failed.");
        	
        }
	}
	
}
