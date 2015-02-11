package edu.utsa.tl13;

public class FactorNode extends BasicNode {

    public IdentNode ident;
    public NumNode num;
    public BoolItNode boolIt;
    public ExpressionNode expr;
    public String typeString="Null";
    

    public void addChild(Node child) {

        if (child instanceof IdentNode) {
            this.ident = (IdentNode) child;
        } else if (child instanceof NumNode) {
            num = (NumNode) child;
        } else if (child instanceof BoolItNode) {
            boolIt = (BoolItNode) child;
        } else if (child instanceof ExpressionNode) {
            expr = (ExpressionNode) child;
        }
    }
    
    public String typeCheck() {        

        if (ident != null) {
            this.typeString = ident.typeCheck();
            if(this.typeString.equalsIgnoreCase("Error"))
            	this.type_OK = false;
        } else if (num != null) {
            this.typeString = num.typeCheck();
            if(this.typeString.equalsIgnoreCase("Error"))
            	this.type_OK = false;
        } else if (boolIt != null) {
            this.typeString = boolIt.typeCheck();
            if(this.typeString.equalsIgnoreCase("Error"))
            	this.type_OK = false;
        } else if (expr != null) {
            this.typeString = expr.typeCheck();
            if(this.typeString.equalsIgnoreCase("Error"))
            	this.type_OK = false;
        } else {
            this.typeString = "Error";
        	this.type_OK = false;
        }

        return this.typeString;
    }
    
    public void draw(int nodeId) {

        if (ident != null) {
            ident.draw(nodeId);
        } else if (num != null) {
            num.draw(nodeId);
        } else if (boolIt != null) {
            boolIt.draw(nodeId);
        } else if (expr != null) {
            expr.draw(nodeId);
        }
    }
}