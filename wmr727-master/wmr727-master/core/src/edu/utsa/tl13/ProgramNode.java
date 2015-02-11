package edu.utsa.tl13;

public class ProgramNode extends BasicNode {

	    public DeclarationListNode declList;
	    public StatementListNode stmtList;

	    

	    public void addDeclList(Node child) {
	        if (child instanceof DeclarationListNode) {
	            this.declList = (DeclarationListNode) child;
	           
	        }
	        
	    }
	    public void addStmtList(Node child){
	    	if (child instanceof StatementListNode) {
	            this.stmtList = (StatementListNode) child;
	        }
	    }
	    public Boolean typeChecking(){
	    		if(this.declList.typeChecking() && this.stmtList.typeChecking()){
	    			this.type_OK = true;
	    			return this.type_OK;
	    		}
	    		else {
	    			this.type_OK = false;
	    			return this.type_OK;
	    		}
	    }
	    
	    public void draw(int nodeId) {

	        Compiler.astOut.writeToFile("program", nodeId, this.type_OK);
	        
	        declList.draw(nodeId);
	        stmtList.draw(nodeId);
	    }
}
