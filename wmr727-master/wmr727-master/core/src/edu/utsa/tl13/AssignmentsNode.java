package edu.utsa.tl13;

public class AssignmentsNode extends StatementNode{
    
    public IdentNode ident;
    public ExpressionNode expr;
    public ReadIntNode readInt;


    public void addChild(Node child) {
        if (child instanceof IdentNode) {
            this.ident = (IdentNode) child;
        }
        else if (child instanceof ExpressionNode) {
            this.expr = (ExpressionNode) child;
        }
        else if (child instanceof ReadIntNode) {
            this.readInt = (ReadIntNode) child;
        }
    }

    public Boolean typeCheck(){

        if (SymbolTable.symTable.containsKey(ident.TokenValue)) {
            if (expr != null) {
                if (ident.typeCheck().equalsIgnoreCase(expr.typeCheck())) {
                    this.type_OK = true;
                   // System.out.println("In Assignment type check " + ident.TokenValue);
                }
                else
                    this.type_OK = false;
            }
            else if (readInt != null) {
                if (ident.typeCheck().equalsIgnoreCase("int")) {
                    this.type_OK = true;
                    //System.out.println("In Assignment type check " + ident.TokenValue);
                }
                else
                    this.type_OK = false;
            }
        }
        else {
            this.type_OK = false;
            ident.typeCheck();
            if (readInt != null) {
                if (ident.typeCheck().equalsIgnoreCase("int")) {
                    this.type_OK = true;
                    //System.out.println("In Assignment type check " + ident.TokenValue);
                }
            } else
            		expr.typeCheck();

        }
       // System.out.println("In Assignment type check " + ident.TokenValue);
        return this.type_OK;
    }
    
   public void draw(int nodeId) {
        
        Compiler.astNodeCounter++;
        
        if (readInt != null) {
        	Compiler.astOut.writeToFile(":=readInt", Compiler.astNodeCounter, this.type_OK);
        }
        else {
        	Compiler.astOut.writeToFile(":=", Compiler.astNodeCounter, this.type_OK);
        }
        
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);
        
        nodeId = Compiler.astNodeCounter;
        
        ident.draw(nodeId);
                        
        if (expr != null) {
            expr.draw(nodeId);
        }       
        
    }
}