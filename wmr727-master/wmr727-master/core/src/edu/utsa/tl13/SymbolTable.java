package edu.utsa.tl13;
import java.util.HashMap;

public class SymbolTable {

	private static SymbolTable instance;
	
    public static HashMap<String, String> symTable;

    public SymbolTable(){
    	symTable = new HashMap<String,String>();
    }
	public static SymbolTable getInstance(){
		if (instance == null)
			instance = new SymbolTable();
		return instance;
	}
    public void insert(String variable, String type, DeclarationsNode node) {
        if (!symTable.containsKey(variable)) {
            symTable.put(variable, type);
            //System.out.println("Inserted " + variable + type);
        }
        else {
            node.type_OK = false;
            System.err.println("Syntax Error: Duplicate Variable error : " + node.TokenValue);
        }
    }

}