package edu.utsa.tl13;
import java.util.ArrayList;
import java.util.Iterator;

public class StatementListNode extends BasicNode{

    public ArrayList<StatementNode> stmts;
    
    public StatementListNode(){
    	this.stmts =  new ArrayList<StatementNode>();
    }
    
    public void addChild(Node child) {
    	if (child instanceof StatementNode) {
    		this.stmts.add((StatementNode) child);
    	}
    }
    public Boolean typeChecking(){
    	
        Iterator<StatementNode> itr = stmts.iterator();
        while (itr.hasNext()) {
            StatementNode st = (StatementNode) itr.next();
            if (!st.typeCheck()) {
            	 //System.out.println("In stmsList type check " );
                this.type_OK = false;
                
            } 
   
          }
       // this.type_OK = true;
        return this.type_OK;
    }
    
    public void draw(int nodeId) {

        Compiler.astNodeCounter++;
        
        Compiler.astOut.writeToFile("stmt list", Compiler.astNodeCounter,  this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);

        nodeId = Compiler.astNodeCounter;
        Iterator<StatementNode> itr = stmts.iterator();
        while (itr.hasNext()) {
            StatementNode st = (StatementNode) itr.next();
            st.draw(nodeId);
        }
    }
}
