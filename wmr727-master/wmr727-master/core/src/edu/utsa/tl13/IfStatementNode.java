package edu.utsa.tl13;

public class IfStatementNode extends StatementNode {

    ExpressionNode expr;
    ThenStatementNode thenStmt = null;
    ElseStatementNode elseStmt = null;


    public void addChild(Node child) {

        if (child instanceof ExpressionNode) {
            this.expr = (ExpressionNode) child;
        } else if (child instanceof ThenStatementNode) {
            this.thenStmt = (ThenStatementNode) child;
        } else if (child instanceof ElseStatementNode) {
            this.elseStmt = (ElseStatementNode) child;
        }
    }
    
    public Boolean typeCheck() {
        Boolean thenType = null;
        Boolean elseType = null;

        String expressionType = expr.typeCheck();
        if (thenStmt != null) {
            thenType = thenStmt.typeCheck();
        }
        if (elseStmt != null) {
            elseType = elseStmt.typeCheck();
        }
        if (!expressionType.equalsIgnoreCase("bool")
                || (thenType != null && !thenType)
                || (elseType != null && !elseType)) {
            this.type_OK = false;
            System.err.println("Syntax Error: expression type mismatch on: If ");
           // System.out.println("ExpreType "+ expressionType);
        }
        //System.out.println("ExpreType "+ expressionType);
        return this.type_OK;
    }
    public void draw(int nodeId) {

        Compiler.astNodeCounter++;
        Compiler.astOut.writeToFile("IF", Compiler.astNodeCounter, this.type_OK);
        Compiler.astOut.writeToFile(nodeId, Compiler.astNodeCounter);


        nodeId = Compiler.astNodeCounter;
        expr.draw(nodeId);

        if (thenStmt != null) {
            thenStmt.draw(nodeId);
        }

        if (elseStmt != null) {
            elseStmt.draw(nodeId);
        }
    }
}