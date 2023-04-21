    public Bot(final String server, final int port) {
        this.port = port;
        try {
            this.server = new Server(new Socket(server, port));
            this.server.sendRaw("NICK " + getNick() + "\n");
            this.server.sendRaw("USER " + getUser() + " 0 * :" + getRealName());
            if (this instanceof IRCEventListener) {
                this.server.getEventManager().addListener((IRCEventListener) this);
            }
            onStart();
            for (Channel s : getChannels()) {
                s.join();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
