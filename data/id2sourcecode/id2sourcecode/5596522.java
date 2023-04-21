    public static void toParseTree(StringBuilder sb, int indent, ParseNode node) {
        for (int x = 0; x < indent; x++) {
            sb.append("  ");
        }
        sb.append("( ");
        sb.append(node.getRule());
        sb.append("\n");
        for (Object child : node._children) {
            if (child instanceof ParseNode) {
                toParseTree(sb, indent + 1, (ParseNode) child);
            } else if (child instanceof Token) {
                Token token = (Token) child;
                if (token.getChannel() != token.HIDDEN_CHANNEL) {
                    for (int x = 0; x < indent + 1; x++) {
                        sb.append("  ");
                    }
                    sb.append(token.getType());
                    sb.append(":");
                    sb.append(token.getText());
                    sb.append("\n");
                }
            }
        }
        for (int x = 0; x < indent; x++) {
            sb.append("  ");
        }
        sb.append(")");
        sb.append("\n");
    }
