    private int midPointPartition(Token[] tokens, int p, int r) {
        int i = (p + r) / 2;
        Token temp = tokens[r];
        tokens[r] = tokens[i];
        tokens[i] = temp;
        return partitions(tokens, p, r);
    }
