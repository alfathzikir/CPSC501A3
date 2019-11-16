/*
 * Name: Alfath Zikir
 * UCID: 30027320
 * Assignment 3
 * Class: Deserializer
 **/


import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.lang.Integer;

import org.jdom2.Document;
import org.jdom2.Element;


public class Deserializer {

	public static Object deserialize(Document doc) {

		Element rootElement = doc.getRootElement();
		List<Element> objectList = rootElement.getChildren();
	    HashMap objectMap =  new HashMap();
	
	    createInstances(objectMap, objectList);
		setFieldValues(objectMap,objectList);
		Object object = objectMap.get("0");
	
		
		return object;
	}

	private static void createInstances(HashMap objectMap, List objectList) {

		for (int i = 0; i < objectList.size(); i++) {
			
			try {
				
				Element objectElement = (Element) objectList.get(i);
				Class c = Class.forName(objectElement.getAttributeValue("class"));
				
				Object objectInstance = null;
				
				if(!c.isArray()) {
					
					Constructor constructor = c.getConstructor(null);
					
					if(!Modifier.isPublic(constructor.getModifiers())) {
						constructor.setAccessible(true);
					}
					objectInstance = constructor.newInstance(null);
					
				}else {
					
					Class arrayType = c.getComponentType();
					int arrayLength = Integer.parseInt(objectElement.getAttributeValue("length"));
					
					objectInstance = Array.newInstance(arrayType, arrayLength);
					
				}
				
				objectMap.put(objectElement.getAttributeValue("id"), objectInstance);
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	private static void setFieldValues(HashMap objectMap, List objectList) {
		
		for (int i = 0; i < objectList.size(); i++) {
			
			try {
				
				Element objectElement = (Element) objectList.get(i);

                Object objectInstance =  objectMap.get(objectElement.getAttributeValue("id"));
				
				List fieldElements = objectElement.getChildren();
				
				Class c = objectInstance.getClass();
				
				if(!c.isArray()) {
					 
					for(int j = 0; j < fieldElements.size(); j++) {
						Element fieldElement = (Element) fieldElements.get(j);
						
						Class declaringClass = Class.forName(fieldElement.getAttributeValue("declaringclass"));
						String fieldName = fieldElement.getAttributeValue("name");
						Field f = declaringClass.getDeclaredField(fieldName);
						
						if(!Modifier.isPublic(f.getModifiers())) {
							f.setAccessible(true);
							
                            Field modifiersField = Field.class.getDeclaredField("modifiers");
                            modifiersField.setAccessible(true);
                            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                           

						}
						
						Class fieldType = f.getType();
						Object fieldContent = deserializeValue(fieldType, (Element) fieldElement.getChildren().get(0), objectMap);
						f.set(objectInstance, fieldContent);
						
					}
					
				}else {
					
					Class arrayType = objectInstance.getClass().getComponentType();
					
					for (int j = 0; j < fieldElements.size(); j++) {
						
					    Object arrayContent = deserializeValue(arrayType, (Element) fieldElements.get(j), objectMap);
						Array.set(objectInstance, j, arrayContent);
						
					}
					
					
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	private static Object deserializeValue(Class arrayType, Element valueElement, HashMap objectMap) {

		
		String valueType = valueElement.getName();
		String value = valueElement.getText();
		
		if(valueType.equals("value")) {
			
            if (value.equals("invalid")) {
                return null;
            }
            
            if(arrayType.equals(boolean.class)) {
                return Boolean.valueOf(value);
                
            }else if(arrayType.equals(byte.class)) {
                return Byte.valueOf(value);
                
            }else if (arrayType.equals(short.class)) {
                return Short.valueOf(value);
            
			}else if (arrayType.equals(int.class)) {
                return Integer.valueOf(value);
                
			}else if (arrayType.equals(long.class)) {
                return Long.valueOf(value);
                
            }else if (arrayType.equals(float.class)) {
                return Float.valueOf(value);
                
            }else if (arrayType.equals(double.class)) {
                return Double.valueOf(value);
                
            }else if (arrayType.equals(char.class)) {
                return value.charAt(0);
            }
            
        }else{
        	return objectMap.get(value);
        }
		return value;
		

		

	}


		
	
}
