    public static void toString(StringBuilder sb, ParseNode node, boolean hiddenToo) {
        for (Object child : node._children) {
            if (child instanceof ParseNode) {
                toString(sb, (ParseNode) child, hiddenToo);
            } else if (child instanceof Token) {
                Token token = (Token) child;
                if (hiddenToo || token.getChannel() != token.HIDDEN_CHANNEL) {
                    sb.append(token.getText());
                }
            }
        }
    }
