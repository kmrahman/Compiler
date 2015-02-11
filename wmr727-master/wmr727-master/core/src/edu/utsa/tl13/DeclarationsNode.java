package edu.utsa.tl13;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DeclarationsNode extends BasicNode {
	public String declType;
	public int arrayRange = -1;
	public ArrayList<String> mlValues = new ArrayList<String>();

	public Boolean typeCheck(){
		return this.type_OK;
	}
	
    public void draw(int nodeId) {
        
        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile("decl: '" + this.TokenValue + "'", Compiler.astNodeCounter, true);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);
        
        nodeId = Compiler.astNodeCounter;
        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile(declType, Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);
    }
}
