package utilities;

import java.util.List;

public interface RepositorySystem {
	
	public <T> void saveData(T data); //salvataggio dei dati
	public <T> List<T> loadData(Class<T> classType); //caricamento dei dati
	public <T >void addElement(Class<T> classType, T elem); //inserimento di un elemento
	public <T> void removeElement(Class<T> classyType, T item); //rimozione di un elemento
	public <T> void modifyElement(String keyName, String key, T elem); //modifica di un elemento
	public <T> void modifyObject(String keyName, String key, T elem); //modifica di un oggetto
	public <T> boolean existElement(String keyName, String key); //elemento trovato?
	public <T> T searchElement(Class<T> classType, String keyName, String key); //ricerca di un elemento
	public <T> T loadFirstElement(String key); //caricamento del primo elemento
	
}
