package edu.utsa.tl13;
import java.util.ArrayList;

public class ExtendedTermNode extends BasicNode {

    public ArrayList<TermNode> terms;
    
    public ExtendedTermNode() {
    	this.terms = new ArrayList<TermNode>();
    }
 
    public void addChild(Node child) {  
        this.terms.add((TermNode) child);
    }
    
    public String typeCheck() {
        if (terms.get(0).typeCheck().equalsIgnoreCase("int")) {
              return "int";
        } else {
            System.err.println("Type Mismatch error on: " + this.TokenValue);
            this.type_OK = false;
            return "Error";
        }

    }
    
    public void draw(int nodeId) {


        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile(this.TokenValue, Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);

        nodeId = Compiler.astNodeCounter;

        // left 
        TermNode tempTerm = terms.get(1);
        tempTerm.draw(nodeId);

        // right 
        tempTerm = terms.get(0);
        tempTerm.draw(nodeId);
    }
}
