    private boolean segment(int i[], int o[]) throws GrammarException, SemanticException, BreakException, ReturnException {
        int i1[] = { 0 }, i2[] = { 0 }, i3[] = { 0 };
        Object v1[] = { 0 };
        Stack params = new Stack();
        try {
            return x("bk()", i, o) && raiseBreak(i) || x("rt()", i, o) && raiseReturn(i, new Object[] { null }) || x("rt(", i, i1) && eval(v1, i1, i2) && x(")", i2, o) && raiseReturn(i, v1) || read.S(i, o) || write.S(i, o) || assigment.S(i, o) || ifEval.S(i, o) || forEval.S(i, o) || whileEval.S(i, o) || repeatEval.S(i, o) || switchEval.S(i, o) || x(TokenType.IDENTIFIER, i, i1) && x("(", i1, i2) && interv(params, i2, i3) & x(")", i3, o) && run(v1, peekToken(i), params) || x(TokenType.IDENTIFIER, i, o) && run(v1, peekToken(i), params) || !attr(o, i[0]);
        } catch (SyntaticException e) {
            throw new SemanticException(e, peekToken(i));
        }
    }
