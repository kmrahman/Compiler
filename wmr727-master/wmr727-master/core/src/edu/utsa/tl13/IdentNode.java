package edu.utsa.tl13;

public class IdentNode extends BasicNode{
	
	public int arrayIndex = -1;
	
    public String typeCheck() {
        if (!SymbolTable.symTable.containsKey(this.TokenValue)) {
            System.err.println("Syntax Error: No vairable " + this.TokenValue + " Declared.\n");
            this.type_OK = false;
            return "Error";
        } 
       // System.out.println(SymbolTable.symTable.get(this.TokenValue));
        return SymbolTable.symTable.get(this.TokenValue);
    }
    
    public void draw(int nodeId) {

        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile(this.TokenValue, Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);
    }
}
