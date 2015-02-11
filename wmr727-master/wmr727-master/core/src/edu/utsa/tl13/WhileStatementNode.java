package edu.utsa.tl13;

public class WhileStatementNode extends StatementNode {
	    ExpressionNode expr;
	    DoStatementNode doStmt;


	    public void addChild(Node child) {

	        if (child instanceof ExpressionNode) {
	            this.expr = (ExpressionNode) child;
	        } else if (child instanceof StatementNode) {
	            this.doStmt = (DoStatementNode) child;
	        }
	    }
	    public Boolean typeCheck() {

	        String expressionType = expr.typeCheck();
	        Boolean doStatementsType = doStmt.typeCheck();
	        if (!expressionType.equalsIgnoreCase("bool")
	                || !doStatementsType) {
	            this.type_OK = false;
	            System.err.println("Syntax Error: expression type mismatch on: While ");
	        } 
	       // System.out.println("ExpreType "+ expressionType + " "+ this.type_OK);
	        return this.type_OK;
	    }
	    
	    public void draw(int nodeId) {

	        Compiler.astNodeCounter++;
	        Compiler.astOut.writeToFile("While", Compiler.astNodeCounter, this.type_OK);
	        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);


	        nodeId = Compiler.astNodeCounter;
	        expr.draw(nodeId);

	        if (doStmt != null) {
	            doStmt.draw(nodeId);
	        }

	    }
}
