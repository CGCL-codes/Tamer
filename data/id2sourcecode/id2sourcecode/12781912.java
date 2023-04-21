    public void consume() {
        if (p == -1) setup();
        p++;
        sync(p);
        while (tokens.get(p).getChannel() != channel) {
            p++;
            sync(p);
        }
    }
