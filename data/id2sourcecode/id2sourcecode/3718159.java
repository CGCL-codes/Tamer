    protected String serializeToken(Token t) {
        StringBuffer buf = new StringBuffer(50);
        buf.append(t.getTokenIndex());
        buf.append(' ');
        buf.append(t.getType());
        buf.append(' ');
        buf.append(t.getChannel());
        buf.append(' ');
        buf.append(t.getLine());
        buf.append(' ');
        buf.append(t.getCharPositionInLine());
        serializeText(buf, t.getText());
        return buf.toString();
    }
