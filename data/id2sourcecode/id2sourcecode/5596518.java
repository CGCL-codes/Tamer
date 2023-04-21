    public Token getToken(int index) {
        int count = index;
        for (Object temp : _children) {
            if (temp instanceof Token) {
                Token token = (Token) temp;
                if (token.getChannel() != Token.HIDDEN_CHANNEL) {
                    if (count == 0) {
                        return token;
                    } else {
                        count--;
                    }
                }
            }
        }
        return null;
    }
