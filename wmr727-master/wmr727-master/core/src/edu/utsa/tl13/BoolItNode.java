package edu.utsa.tl13;

public class BoolItNode extends BasicNode{

    public String typeCheck() {
    	return	"bool";
    }
    
    public void draw(int nodeId) {
        
        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile(this.TokenValue, Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);
    }
}
