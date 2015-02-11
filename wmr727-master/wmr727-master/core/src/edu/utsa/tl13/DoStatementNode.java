package edu.utsa.tl13;
import java.util.ArrayList;
import java.util.Iterator;

public class DoStatementNode extends StatementNode{
    
    ArrayList<StatementNode> doStmt;

    public DoStatementNode() {
        this.doStmt = new ArrayList<StatementNode>();
    }

    public void addChild(Node child) {
        this.doStmt.add((StatementNode)child);
    }
    
    public Boolean typeCheck() {
        Iterator<StatementNode> itr = doStmt.iterator();

        while (itr.hasNext()) {
            StatementNode stmt =  (StatementNode) itr.next();
            if (!stmt.typeCheck()) {
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
        Iterator<StatementNode> itr = doStmt.iterator();

        while (itr.hasNext()) {
            StatementNode doS = (StatementNode) itr.next();
            doS.draw(nodeId);
        }

    }
}
