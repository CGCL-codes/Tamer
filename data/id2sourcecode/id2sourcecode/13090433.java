    public Window getChannel(String channel) {
        Window win;
        channel = channel.trim();
        win = (Window) channels.get(channel.toUpperCase());
        if (win == null) {
            win = new Window(this, channel, Window.TYPE_CHANNEL, hilight, header, timestamp, usecol, mirccol, buflines);
            channels.put(channel.toUpperCase(), win);
            addWindow(win);
        }
        return win;
    }
