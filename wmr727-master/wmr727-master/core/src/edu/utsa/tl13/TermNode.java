package edu.utsa.tl13;

public class TermNode extends BasicNode {
    public FactorNode fact;
    public ExtendedFactorNode exFact;
    public String typeString;


    public void addChild(Node child) {

    	if (child instanceof FactorNode) {
            this.fact = (FactorNode) child;
        }
    	else if (child instanceof ExtendedFactorNode) {
            this.exFact = (ExtendedFactorNode) child;
    	}
    }
    public String typeCheck() {
        if (exFact != null) {
            if (fact != null) {
                if (fact.typeCheck().equalsIgnoreCase("int")) {
                    this.typeString = exFact.typeCheck();
                    if(this.typeString.equalsIgnoreCase("Error"))
                    	this.type_OK = false;
                } else {
                    exFact.typeCheck();
                    this.typeString = "Error";
                    this.type_OK = false;
                }
            } else {
                this.typeString = "Error";
                this.type_OK = false;
            }
        } else if (fact != null) {
            this.typeString = fact.typeCheck();
            if(this.typeString.equalsIgnoreCase("Error"))
            	this.type_OK = false;
        } else {
            this.typeString = "Error";
        }

        return this.typeString;
    }
    
    public void draw(int nodeId) {

        if (exFact != null) {
            exFact.addChild(fact);
            exFact.draw(nodeId);
        } else if (fact != null) {
            fact.draw(nodeId);
        }
    }
}