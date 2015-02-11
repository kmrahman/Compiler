package edu.utsa.tl13;
import java.util.ArrayList;
import java.util.Iterator;

public class ElseStatementNode extends StatementNode{
	   ArrayList<StatementNode> elseStmt;

	    public ElseStatementNode() {
	        this.elseStmt = new ArrayList<StatementNode>();
	    }
	    

	    public void addChild(Node child) {
	        this.elseStmt.add((StatementNode)child);
	    }
	    
	    public Boolean typeCheck() {
	        Iterator<StatementNode> itr = elseStmt.iterator();

	        while (itr.hasNext()) {
	            StatementNode stmtNode = (StatementNode) itr.next();
	            if (!stmtNode.typeCheck()) {
	                this.type_OK = false;
	            }
	        }
	        return this.type_OK;
	    }

	    public void draw(int nodeId) {

	        Compiler.astNodeCounter++;
	        Compiler.astOut.writeToFile("stmt list", Compiler.astNodeCounter, this.type_OK);
	        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);


	        nodeId = Compiler.astNodeCounter;
	        Iterator<StatementNode> itr = elseStmt.iterator();

	        while (itr.hasNext()) {
	            StatementNode astElse = (StatementNode) itr.next();
	            astElse.draw(nodeId);
	        }

	    }
}
