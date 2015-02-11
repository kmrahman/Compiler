package edu.utsa.tl13;

public class ExpressionNode extends BasicNode {

    public ExtendedExpressionNode exExpr = null;
    public SimpleExpressionNode expr = null;
    String typeString= "Error";

    public void addChild(Node child) {
        if (child instanceof ExtendedExpressionNode) {
            this.exExpr = (ExtendedExpressionNode) child;
        } else if (child instanceof SimpleExpressionNode) {
            this.expr = (SimpleExpressionNode) child;
        }
    }
    
    public String typeCheck() {
        if (exExpr != null) {
            if (expr != null) {
                if (expr.typeCheck().equalsIgnoreCase("int")) {
                    this.typeString = exExpr.typeCheck();
                    if(this.typeString.equalsIgnoreCase("Error"))
                    	this.type_OK = false;
                } else {
                    exExpr.typeCheck();
                    this.type_OK = false;
                }
            } else {
                this.type_OK = false;
            }
        } else if (expr != null) {
            this.typeString= expr.typeCheck();
        } else {
            this.type_OK = false;
            
        }
       // System.out.println("In write int type check ");
        return this.typeString;
    }
    public void draw(int nodeId) {

        if (exExpr != null) {
            exExpr.addChild(expr);
            exExpr.draw(nodeId);
        } else if (expr != null) {
            expr.draw(nodeId);
        }
    }
}