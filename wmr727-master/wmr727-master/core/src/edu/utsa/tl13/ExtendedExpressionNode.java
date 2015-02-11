package edu.utsa.tl13;
import java.util.ArrayList;


public class ExtendedExpressionNode extends BasicNode {

    public ArrayList<SimpleExpressionNode> exprs;
    public String typeString;
    
    public ExtendedExpressionNode(){
    	this.exprs  = new ArrayList<SimpleExpressionNode>();
    }

    public void addChild(Node child) {
        this.exprs.add((SimpleExpressionNode) child); 
    }
    
    public String typeCheck() {
        if (exprs.get(0).typeCheck().equalsIgnoreCase("int")) {
            this.typeString = "bool";
        } else {
            System.err.println("Type mismatch error on : " + this.TokenValue);
            this.typeString = "Error";
            this.type_OK = false;
        }

        return this.typeString;
    }
    
    public void draw(int nodeId) {

        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile(this.TokenValue, Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);

        nodeId = Compiler.astNodeCounter;

        // left operand
        SimpleExpressionNode tempExpr = exprs.get(1);
        tempExpr.draw(nodeId);

        // right operand
        tempExpr = exprs.get(0);
        tempExpr.draw(nodeId);
    }
}
