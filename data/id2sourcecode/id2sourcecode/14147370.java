    @Override
    public void caseANameCommand(ANameCommand node) {
        capturePosition(node.getPosition());
        JavaExpression readNode = callFindVariable(variableTranslator.translate(node.getVariable()), false);
        java.writeStatement(callOn(CONTEXT, "writeEscaped", call("getNodeName", readNode)));
    }
