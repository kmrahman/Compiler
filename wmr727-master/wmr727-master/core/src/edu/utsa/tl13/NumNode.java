package edu.utsa.tl13;

public class NumNode extends BasicNode{

    public String typeCheck() {
        if (this.TokenValue.length() > 10) {
            System.err.println("Syntax Error: Illegal integer on: " + this.TokenValue);
            this.type_OK = false;
            return "Error";
        } 
        else {
            double numValue = Double.parseDouble(this.TokenValue);

         //   System.err.println("Syntax Error: Illegal integer on: " + this.TokenValue + " " + numValue);
            if ((numValue >= 0) && (numValue <= 2147483647)) {
                return "int";
            } else {
                System.err.println("Syntax Error: Illegal integer on: " + this.TokenValue);
                this.type_OK = false;
                return "Error";
            }
        }

    }
    
    public void draw(int nodeId) {

        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile(this.TokenValue, Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);
    }
}
