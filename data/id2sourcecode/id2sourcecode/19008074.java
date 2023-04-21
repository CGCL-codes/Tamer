    protected final void addToken(SynonymToken token) {
        int l = tokens.length - 1;
        for (int i = 0; i < l; i++) tokens[i] = tokens[i + 1];
        tokens[l] = token;
        if (queueSize < tokens.length) queueSize++;
    }
