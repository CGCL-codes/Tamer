    private int removeJunk(int tokenIndex, int direction) {
        while (tokenIndex >= 0 && tokenIndex < input.size()) {
            Token token = input.get(tokenIndex);
            if (token.getChannel() != HIDDEN) switch(token.getType()) {
                case OmttLexer.ACTION_ON_NEWLINE:
                case OmttLexer.EXPRESSION_START:
                case OmttLexer.EXPRESSION_END:
                case OmttLexer.OP_DATA_IS_EXPRESSION:
                case OmttLexer.CONTENT:
                    break;
                default:
                    return tokenIndex;
            }
            tokenIndex += direction;
        }
        return tokenIndex;
    }
