    public int getChannelStatus(int i) {
        char c = ((String) channels.get(i)).charAt(0);
        if (c == '@') {
            return Channel.OPERATOR;
        } else if (c == '+') {
            return Channel.VOICED;
        } else {
            return Channel.NONE;
        }
    }
