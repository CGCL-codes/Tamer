    protected int skipOffTokenChannels(int i) {
        int n = tokens.size();
        while (tokens.get(i).getChannel() != channel) {
            if (i <= n - 1) {
                i++;
            } else {
                readNextToken();
                i++;
            }
        }
        return i;
    }
