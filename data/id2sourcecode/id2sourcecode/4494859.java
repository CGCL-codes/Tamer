    private boolean isWriteReplaceOrReadResolve(DetailAST aAST) {
        final DetailAST ident = aAST.findFirstToken(TokenTypes.IDENT);
        if (!"writeReplace".equals(ident.getText()) && !"readResolve".equals(ident.getText())) {
            return false;
        }
        final DetailAST typeAST = (DetailAST) aAST.findFirstToken(TokenTypes.TYPE).getFirstChild();
        if (typeAST.getType() != TokenTypes.DOT && typeAST.getType() != TokenTypes.IDENT) {
            return false;
        }
        final DetailAST params = aAST.findFirstToken(TokenTypes.PARAMETERS);
        if (params != null && params.getChildCount() != 0) {
            return false;
        }
        final DetailAST throwsAST = aAST.findFirstToken(TokenTypes.LITERAL_THROWS);
        if (throwsAST == null || throwsAST.getChildCount() != 1) {
            return false;
        }
        final DetailAST excpt = (DetailAST) throwsAST.getFirstChild();
        final String exception = FullIdent.createFullIdent(excpt).getText();
        if (!"java.io.ObjectStreamException".equals(exception) && !"ObjectStreamException".equals(exception)) {
            return false;
        }
        return true;
    }
