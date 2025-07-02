package utilities;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonManager {
	private static final String MSG_ERR_NO_DATI = "Nessun dato disponibile...";
	private final File file; 
	private final ObjectMapper objectMapper;
	
	public JsonManager(String filePath) {
		this.file = new File(filePath);
		this.objectMapper = new ObjectMapper();
		this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
		//--> abilita l'identazione del file Json (formattazione Pretty)
		objectMapper.registerModule(new JavaTimeModule());
	}
	
	public <T> void saveData(T data) {
		try {
			objectMapper.writeValue(file, data);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> List<T> loadData(Class<T> classType) {
		try {
			if(!file.exists())
				return new ArrayList<>();
			if(file.length()==0)
				return new ArrayList<>();
			 return objectMapper.readValue(file, 
			            objectMapper.getTypeFactory().constructCollectionType(List.class, classType));
		}catch(IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public <T >void addElement(Class<T> classType, T elem) {
	    try {
	        
	        ArrayNode jsonArray;
	        if (file.exists() && file.length() > 0) 
	            jsonArray = (ArrayNode) objectMapper.readTree(file);
	        else 
	            jsonArray = objectMapper.createArrayNode();
	     
	        jsonArray.add(objectMapper.valueToTree(elem));	

	        saveData(jsonArray);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public <T> void viewData(Class<T> classType) {
		List<T> list = loadData(classType);
		
		if(list.isEmpty())
			System.out.println(MSG_ERR_NO_DATI);
		else {
			for(T elem : list) {	
				System.out.println(elem);
			}
				
		
		}
	}
	
	public <T> void removeElement(Class<T> classyType, T item) {
		try {
			JsonNode rootNode = objectMapper.readTree(file);
			
			if(rootNode != null && rootNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) rootNode;
				ArrayNode newArray = objectMapper.createArrayNode();
				JsonNode specialField = null; 
				//verifico se il primo elemento ha un solo campo (si tratta quindi dell'intestazione del file Json)
				if(arrayNode.size() > 0 && arrayNode.get(0).isObject() && arrayNode.get(0).size() == 1)
					specialField = arrayNode.get(0);	//memorizzo l'elemento
	           
				//rimuovo l'elemento desiderato, saltando il campo speciale in caso ci fosse
				for(JsonNode node : arrayNode) {
					if(specialField != null && node.equals(specialField))
						continue;
					
					if(!node.equals(objectMapper.valueToTree(item))) {
						newArray.add(node);
					}
				}
				
				//reinserisco l'elemento speciale se c'è
				if(specialField != null)
					newArray.insert(0, specialField);
				
				objectMapper.writeValue(file, newArray);
				
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void modifyElement(String keyName, String key, T elem) {
		try {
			
			JsonNode rootNode = objectMapper.readTree(file);

			if(rootNode != null && rootNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) rootNode; 
				
				for(int i = 0; i < arrayNode.size(); i++) {
					JsonNode node = arrayNode.get(i);
					if(node.has(keyName) && node.get(keyName).asText().equals(key)) {
						arrayNode.set(i, objectMapper.valueToTree(elem));	
					}	
				}
				
				objectMapper.writeValue(file, arrayNode);
			}
	
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void modifyObject(String keyName, String key, T elem) {
		try {
	
			JsonNode rootNode = objectMapper.readTree(file);

			if(rootNode != null && rootNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) rootNode; 
				
				for(int i = 0; i < arrayNode.size(); i++) {
					JsonNode node = arrayNode.get(i);
					if(node.has(keyName) && node.get(keyName).asText().equals(key)) {
						if(node.isObject()) {
							
							ObjectNode objectNode = (ObjectNode) node;
							objectNode.set(keyName, objectMapper.valueToTree(elem));
							objectMapper.writeValue(file, objectNode);
						}
					}	
				}
				
				objectMapper.writeValue(file, arrayNode);
			}
	
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public <T> boolean searchElement(String keyName, String key) {
		try {
			JsonNode rootNode = objectMapper.readTree(file);
			
			if(rootNode != null && rootNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) rootNode; 
				
				for(int i = 0; i < arrayNode.size(); i++) {
					JsonNode node = arrayNode.get(i);
					
					if(node.has(keyName) && node.get(keyName).asText().equals(key))
						return true;
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public <T> T searchElement(Class<T> classType, String keyName, String key) {
		try {
			JsonNode rootNode = objectMapper.readTree(file); //radice= JSON convertito in albero
			
			//controllo che vada tutto bene con il file
			if(rootNode != null && rootNode.isArray()) {
				//converto in array l'albero per ciclare 
				ArrayNode arrayNode = (ArrayNode) rootNode; 
				for(int i = 0; i < arrayNode.size(); i++) {
					//elemento da analizzare
					JsonNode node = arrayNode.get(i);
					
					//controllo se la chiave dell'oggetto corrente(node) coincide con quella dell'elemento 
					if(node.has(keyName) && node.get(keyName).asText().equals(key))
						return objectMapper.treeToValue(node, classType); 
				}
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		//elemento non trovato
		return null;
	}
	
	public <T> T loadFirstElement(String key) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if(!file.exists())
				return null;
			if(file.length()==0)
				return null;
			
			List<Map<String, Object>> list = objectMapper.readValue(file, List.class);
			Map<String, Object> firstElement = list.get(0);
		
			 return (T) firstElement.get(key);
			
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
