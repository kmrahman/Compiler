package edu.utsa.tl13;

public class SimpleExpressionNode extends BasicNode {

    public ExtendedTermNode exTerm= null;
    public TermNode term = null;
    public String typeString;

    public void addChild(Node child) {
        if (child instanceof ExtendedTermNode) {
            this.exTerm = (ExtendedTermNode) child;
        } else if (child instanceof TermNode) {
            this.term = (TermNode) child;
        }
    }
    public String typeCheck() {
        if (exTerm != null) {
            if (term != null) {
                if (term.typeCheck().equalsIgnoreCase("int")) {
                    this.typeString = exTerm.typeCheck();
                    if(this.typeString.equalsIgnoreCase("Error"))
                    	this.type_OK = false;
                } else {
                    exTerm.typeCheck();
                    this.typeString = "Error";
                    this.type_OK = false;
                }
            } else {
                this.typeString = "Error";
                this.type_OK = false;
            }
        } else if (term != null) {
            this.typeString = term.typeCheck();
            if(this.typeString.equalsIgnoreCase("Error"))
            	this.type_OK = false;
        } else {
            this.typeString = "Error";
        }
        return this.typeString;
    }
    
    public void draw(int nodeId) {

        if (exTerm != null) {
            exTerm.addChild(term);
            exTerm.draw(nodeId);
        } else if (term != null) {
            term.draw(nodeId);
        }
    }
}