    protected String getChannelValue(String line) {
        int column = channelColumn;
        if (hasTimeToken(line)) {
            column++;
        }
        int i = 1;
        for (Scanner s = new Scanner(line); s.hasNext(); i++) {
            if (i == column) {
                return s.next();
            } else {
                s.next();
            }
        }
        throw new RuntimeException(channel + " value could not be found.");
    }
