package edu.utsa.tl13;

public class WriteIntNode extends StatementNode {

    public ExpressionNode expr;

    public void addChild(Node child) {
        this.expr = (ExpressionNode) child;
    }

    public Boolean typeCheck() {
        if (!expr.typeCheck().equalsIgnoreCase("int")) {
        	 System.err.println("Syntax Error: Type mismatch on: Write Int ");
            this.type_OK = false;
        } 
       // System.out.println("In write int type check ");
        return this.type_OK;
    }
    
    public void draw(int nodeId) {
        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile("WriteInt", Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);



        nodeId = Compiler.astNodeCounter;
        expr.draw(nodeId);
    }
}
