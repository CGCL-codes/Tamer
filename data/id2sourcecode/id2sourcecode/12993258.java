    public int findTokenInSuper(int lineStart, int desiredToken) {
        int lookAhead = 0;
        int tk, channel;
        do {
            lookAhead++;
            Token token = LT(lookAhead);
            tk = token.getType();
            channel = token.getChannel();
        } while ((tk != FortranLexer.EOF && tk != FortranLexer.T_EOS && tk != desiredToken) || channel == lexer.getIgnoreChannelNumber());
        if (tk == desiredToken) {
            return lookAhead;
        }
        return -1;
    }
