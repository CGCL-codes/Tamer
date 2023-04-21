    public PythonTree(int ttype, Token t) {
        CommonToken c = new CommonToken(ttype, t.getText());
        c.setLine(t.getLine());
        c.setTokenIndex(t.getTokenIndex());
        c.setCharPositionInLine(t.getCharPositionInLine());
        c.setChannel(t.getChannel());
        c.setStartIndex(((CommonToken) t).getStartIndex());
        c.setStopIndex(((CommonToken) t).getStopIndex());
        node = new CommonTree(c);
    }
