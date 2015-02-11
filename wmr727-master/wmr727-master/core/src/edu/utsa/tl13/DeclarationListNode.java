package edu.utsa.tl13;
import java.util.ArrayList;
import java.util.Iterator;


public class DeclarationListNode extends BasicNode{

    public ArrayList<DeclarationsNode> declarations;
    
    public DeclarationListNode() {
    	this.declarations = new ArrayList<DeclarationsNode>();
    }
    
    public void addChild(Node child) {
    	if (child instanceof DeclarationsNode) {
    		this.declarations.add((DeclarationsNode) child);
    	}
    }
    public Boolean typeChecking(){
    	
        Iterator<DeclarationsNode> itr = declarations.iterator();
        while (itr.hasNext()) {
            DeclarationsNode declara = (DeclarationsNode) itr.next();
            if (!declara.typeCheck()) {
                this.type_OK = false;
                return false;
            } 
        }

        return this.type_OK;
    }
    
    public void draw(int nodeId) {
        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile("decl list", Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);
        
        nodeId = Compiler.astNodeCounter;
        Iterator<DeclarationsNode> itr = declarations.iterator();
        
        while (itr.hasNext()) {
            DeclarationsNode dec = (DeclarationsNode) itr.next();
            dec.draw(nodeId);
        }
    }
}
