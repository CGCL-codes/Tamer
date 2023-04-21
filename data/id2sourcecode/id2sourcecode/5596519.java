    public void convertToJDBCParam() {
        for (Object o : _children) {
            if (o instanceof Token) {
                Token token = (Token) o;
                if (token.getChannel() != Token.HIDDEN_CHANNEL) {
                    token.setText("?");
                    break;
                }
            }
        }
    }
