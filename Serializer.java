import java.lang.reflect.*;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;

public class Serializer {
		
	public static Document serialize(Object object) {
		
		Element root = new Element("serialized");
		Document doc = new Document(root);
		IdentityHashMap<Object, Integer> objectMap = new IdentityHashMap<Object, Integer>();
		
		return serializeManager(object, doc, objectMap);
	}

	
	private static Document serializeManager(Object object, Document doc, IdentityHashMap<Object, Integer> objectMap) {
	
		
        try {
        	
            // Apply id to each object
    		int objectID = objectMap.size();
            objectMap.put(object, objectID);

            // Create object element
            Element objectElement = new Element("object");

            objectElement.setAttribute("id", Integer.toString(objectID));
            doc.getRootElement().addContent(objectElement);

            
            if(object != null) {
            	
        		Class objectClass = object.getClass();
        		objectElement.setAttribute("class", objectClass.getName());
        		
        		serializeObject(objectClass, objectElement, object, doc, objectMap);
        		
        		
            }else {
            	Element nullElement = new Element("null");
            	objectElement.addContent(nullElement);
            }
            
            

        } catch (Exception e) {e.printStackTrace();}

		return doc;
	}


	private static Element serializeObject(Class objectClass, Element objectElement, Object object, Document doc,  IdentityHashMap<Object, Integer> objectMap) {

        
        if(objectClass.isPrimitive()){
        	
        	objectElement.addContent(handlePrimitive(object));
    
        }else if(objectClass.isArray()) {
        	
        	int length = Array.getLength(object);
        	objectElement.setAttribute("length", Integer.toString(length));
        	
        	handleArray(objectElement, object, doc, objectMap);
        	
        }else {
        	handleFieldElements(objectElement, object, doc, objectMap);
        }
		return objectElement;
        
	}


	private static Element handleFieldElements(Element objectElement, Object object, Document doc,  IdentityHashMap<Object, Integer> objectMap) {
		
		Field[] objectFields = object.getClass().getDeclaredFields();
		
		for(Field f : objectFields) {
			
			try {
				
		        if (!Modifier.isStatic(f.getModifiers())) {
 		           f.setAccessible(true);
 		           
 		           Element fieldElement = new Element("field");
 		           fieldElement.setAttribute("name", f.getName());
 		           fieldElement.setAttribute("declaringclass", f.getDeclaringClass().getName());
 		           
 		           Class fieldType = f.getType();
 		           
 		           Object fieldObject = null;
 		           
 		           try {  
 		               fieldObject = f.get(object);
 		           } 
 		           catch(IllegalArgumentException | IllegalAccessException e) {
 		               e.printStackTrace();
 		           }
 		           
 		           Element fieldContent = null;
 		           
 		           if(Modifier.isTransient(f.getModifiers())) {
 		               fieldContent = new Element("transient");
 		           
 		           } else if (fieldType.isPrimitive()) {
 		               fieldContent = handlePrimitive(fieldObject);
 		           } else {
		               fieldContent = handleReference(fieldObject, fieldType, doc, objectMap);
 		           }
 		           
 		           fieldElement.addContent(fieldContent);
 		           
 		           objectElement.addContent(fieldElement);
					
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return objectElement;
	}


	private static Element handlePrimitive(Object object) {
    	
		Element value = new Element("value");
		
		try {
    		value.setText(object.toString());
            
        }catch(Exception e) {
        		
        	e.printStackTrace();
        }
		return value;
        
	}

	
	private static Element handleArray(Element objectElement, Object object, Document doc,  IdentityHashMap<Object, Integer> objectMap) {
		
	
		int length = Array.getLength(object);
		
		ArrayList<Element> arrayElements = new ArrayList<>();
		
		Class arrayType = object.getClass().getComponentType();
		
		for(int i = 0; i < length; i++) {
			
			Object arrayElementObject = Array.get(object, i);
			Element arrayElement = null;
			
			if(arrayType.isPrimitive()) {
				arrayElement = handlePrimitive(arrayElementObject);
			}else {
				arrayElement = handleReference(arrayElementObject, arrayType, doc, objectMap);
			}
			arrayElements.add(arrayElement);
		}
		
		for(Element e : arrayElements) {
			objectElement.addContent(e);
		}
		return objectElement;
	}	


	private static Element handleReference(Object arrayElementObject, Class arrayType, Document doc,
			IdentityHashMap<Object, Integer> objectMap) {

		Element reference = new Element("reference");
		
        if(objectMap.containsKey(arrayElementObject)) {
        	reference.setText(objectMap.get(arrayElementObject).toString());
        }else {
        	reference.setText(Integer.toBinaryString(objectMap.size()));
        	serializeManager(arrayElementObject, doc, objectMap);
        }
		
		return reference;
	}
		

}
