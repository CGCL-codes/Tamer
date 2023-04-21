    public int getLineLength(int start) {
        int lineLength;
        Token token;
        lineLength = 0;
        if (start >= super.tokens.size()) return lineLength;
        do {
            token = super.get(start + lineLength);
            lineLength++;
        } while ((start + lineLength) < super.tokens.size() && (token.getChannel() == lexer.getIgnoreChannelNumber() || token.getType() != FortranLexer.T_EOS && token.getType() != FortranLexer.EOF));
        return lineLength;
    }
