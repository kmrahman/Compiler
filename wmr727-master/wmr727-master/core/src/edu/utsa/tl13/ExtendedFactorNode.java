package edu.utsa.tl13;
import java.util.ArrayList;

public class ExtendedFactorNode extends BasicNode {

    public ArrayList<FactorNode> factors;
    
    public ExtendedFactorNode(){
    	this.factors = new ArrayList<FactorNode>();
    }


    public void addChild(Node child) {  
        this.factors.add((FactorNode) child);
    }
    
    public String typeCheck() {
        if (factors.get(0).typeCheck().equalsIgnoreCase("int")) {
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

        // left operand
        FactorNode tempFact = factors.get(1);
        tempFact.draw(nodeId);

        // right operand
        tempFact = factors.get(0);
        tempFact.draw(nodeId);
    }
}

