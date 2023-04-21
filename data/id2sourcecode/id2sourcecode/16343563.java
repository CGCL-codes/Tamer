    private void connect() {
        this.server.sendRaw("NICK " + getNick() + "\n");
        this.server.sendRaw("USER " + getNick() + " metalmonster.me TB: Speed Bot\n");
        for (Channel s : getChannels()) {
            s.join();
        }
    }
