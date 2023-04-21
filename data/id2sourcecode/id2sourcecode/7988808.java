    public void init(BufferedReader bufferedreader, PrintWriter printwriter, IRCListener irclistener) {
        if (bufferedreader == null || printwriter == null || irclistener == null) {
            throw new NullPointerException();
        } else {
            reader = bufferedreader;
            writer = printwriter;
            listener = irclistener;
            listener.setProtocolHandler((IRCClient) this);
            return;
        }
    }
