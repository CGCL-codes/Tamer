    public static Channel getChannel(int number) {
        Channel c = channels.get(number);
        if (c == null) {
            c = new Channel(number);
            channels.put(number, c);
        }
        return c;
    }
