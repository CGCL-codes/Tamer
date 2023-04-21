    protected int getLeftmostDerivation(StringBuffer buf, int step) {
        int numReplacements = 0;
        if (step <= 0) {
            buf.append(' ');
            buf.append(toString());
            return numReplacements;
        }
        AST child = getFirstChild();
        numReplacements = 1;
        while (child != null) {
            if (numReplacements >= step || child instanceof ParseTreeToken) {
                buf.append(' ');
                buf.append(child.toString());
            } else {
                int remainingReplacements = step - numReplacements;
                int n = ((ParseTree) child).getLeftmostDerivation(buf, remainingReplacements);
                numReplacements += n;
            }
            child = child.getNextSibling();
        }
        return numReplacements;
    }
