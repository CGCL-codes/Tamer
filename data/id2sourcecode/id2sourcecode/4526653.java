    public char getChannel(int channel) {
        synchronized (scopes) {
            return levels[(int) scopes[channel]];
        }
    }
