    private boolean ensureSize(int size) {
        if (size < tokens.size()) {
            return true;
        }
        while (tokens.size() < size) {
            Token nextToken = tokenSource.nextToken();
            if (nextToken == Token.EOF_TOKEN) {
                return false;
            }
            if (nextToken.getChannel() == channel) {
                ((XQToken) nextToken).spaceBefore = spaceBefore;
                spaceBefore = false;
                tokens.add(nextToken);
            } else {
                spaceBefore = true;
            }
        }
        return true;
    }
