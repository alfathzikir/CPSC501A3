/*
 * Name: Alfath Zikir
 * UCID: 30027320
 * Assignment 3
 * Class: Inspector
 **/


import java.lang.reflect.*;
import java.util.*;

public class Inspector {

	public Inspector() {
		
	}
	
    public void inspect(Object obj, boolean recursive) {
		Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    		
    	//Getting the name of declaring class
    	System.out.println("Declaring class:\t" + obj);
    	
    	//Getting the superclass
    	String superClassName = c.getSuperclass().getName();
    	System.out.println("SuperClass: \t" + superClassName);
    	
    	Class superClass = c.getSuperclass();
    	inspectSuperclasses(superClass, c, obj, recursive);

    	//Getting the interfaces
        System.out.print("Interfaces: ");
        inspectInterfaces(c, obj, recursive);
    	
    	//Getting the methods
        Method method[] = c.getDeclaredMethods();
        inspectMethods(method);
        
    	//Getting the constructors
        Constructor constructor[] = c.getConstructors();
        inspectConstructors(constructor);
        
		//Getting the fields
        System.out.println("Class Fields:");
        inspectFields(c, obj, recursive);
    	
    	//Getting the information from traversing the super classes
    	
    	//Check for arrays
        try {
        	//Check if initial object is an array
        	if (c.isArray()) {
               //Print out type and length if object is an array
        		System.out.println("Array Type: " + c.getComponentType());
                System.out.println("Array Length: " + Array.getLength(obj));
                //Print out element of arrays.
                for (int i = 0; i < Array.getLength(obj); i++) {
                	System.out.println("Element " + i + ": " + Array.get(obj, i));
                }
            }
        }catch (Exception e ) { System.out.println(e.getMessage()); }
    }
    

    public void inspectSuperclasses(Class s, Class c, Object obj, boolean recursive){
    	
        System.out.println("Super Classes:");
        
        //Get methods of superclass
        Method method[] = s.getDeclaredMethods();
        inspectMethods(method);
        
        //Get constructors
        Constructor constructor[] = s.getConstructors();
        inspectConstructors(constructor);
        
        //Get Fields
        inspectFields(c, obj, recursive);

        //Check for recursive
        if (s != null)
            System.out.println("Recursive Super Classes: \n");

        while (s.getSuperclass() != null) {
            Class nextClass = s.getSuperclass();
            System.out.println(nextClass.getName() + ": \n");

            Method m[] = nextClass.getDeclaredMethods();
            inspectMethods(m);
            
            Constructor constructors[] = nextClass.getConstructors();
            inspectConstructors(constructors);
            
            inspectFields(nextClass, obj, recursive);
            
            s = nextClass;
        }

}


    public void inspectInterfaces(Class c, Object obj, boolean recursive){
        Class[] interfaces = c.getInterfaces();
       
        for (Class i : interfaces) {
        	//Get name of interface
            System.out.println("Interface : " + i.getName() + "\n");

            //Get interface methods
            if (i.getMethods().length != (new Method[] {}).length){
                System.out.println("Interface Methods: \n");
                inspectMethods(i.getMethods());
            }

            //Get interface constructors
            if (i.getConstructors().length != (new Constructor[] {}).length){
                System.out.println("Constructos: \n");
                inspectConstructors(i.getConstructors());
            }

            //Get interface FIELDS
            if (i.getConstructors().length != (new Constructor[] {}).length){
                System.out.println("Interface: \n");
                inspectFields(i, obj, recursive);
            }
            
            //Check for recursive
            if (i.getInterfaces().length != (new Method[] {}).length) {
                System.out.println("");
            }

            while (i.getInterfaces().length != (new Method[] {}).length) {
                for (Class recursiveInterface : i.getInterfaces()) {
                    inspectInterfaces(recursiveInterface, obj, recursive);
                }
            }

        }
    }

    public void inspectMethods(Method[] method) {
        for(Method methods : method){
        	
        	//Get exceptions
            Class[] exceptions = methods.getExceptionTypes();
            for (Class exception : exceptions) {
                System.out.println("Exception: " + exception.getName());
            }

            //Get the parameter types
            Class[] parameters = methods.getParameterTypes();
            for (Class parameter : parameters) {
                System.out.println("Parameter: " + parameter.getName());
            }

            //Get the Return type
            Class returnType = methods.getReturnType();
            System.out.println("Return type : " + returnType.getName());

            //Get the Modifiers
            int modifiers = methods.getModifiers();
    		System.out.println("Modifiers: \t" + modifiers + "\n");
            System.out.println();


        }
    }

    public void inspectConstructors(Constructor[] constructor) {
    	
    	for (Constructor constructors : constructor) {
    		
    		//get the parameters for the constructors
    		Class[] parameters = constructors.getParameterTypes();
    		for (Class parameter: parameters) {
    			System.out.println("Parameter: \t" + parameter.getName());
    		}
    		
    		//Get modifiers
    		int modifiers = constructors.getModifiers();
    		System.out.println("Modifiers: \t" + modifiers + "\n");
    		
    	}

    }

    public void inspectFields(Class c, Object obj, boolean recursive) {

        Field[] declaredFields = c.getDeclaredFields();

        try {
        	//Printing Name, Type, Modifier of fields
            for (Field f : declaredFields){
                System.out.println("Field: " + f.getName());
                System.out.println("Type: " + f.getType().getName());
                System.out.println("Modifier: " + f.getModifiers());

                //Makes field to be accessible
                f.setAccessible(true);
                
                //Printing out the value of the field
                System.out.println("Value: " + f.get(obj) + "\n");
            }

            //Recursive Inspection
            if (recursive) {
                System.out.println("Check if recursive");
                Vector<Field> objectsToInspect = new Vector();

                for (Field f : declaredFields) {
                    if(! f.getType().isPrimitive() ) 
		                objectsToInspect.addElement(f);
                }

                for (Field field : objectsToInspect) {
                    inspect(field.get(obj), recursive);
                }
            }
        }
        catch (Exception e) { System.out.println(e.getMessage()); }

    }
}