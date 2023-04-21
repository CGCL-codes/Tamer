    public int getNumberOfHiddenTokens(int i, int j) {
        int n = 0;
        TokenStream input = parser.getTokenStream();
        for (int ti = i; ti < input.size() && ti <= j; ti++) {
            Token t = input.get(ti);
            if (t.getChannel() != Token.DEFAULT_CHANNEL) {
                n++;
            }
        }
        return n;
    }
