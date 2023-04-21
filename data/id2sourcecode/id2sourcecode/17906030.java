    public String getWindowName() {
        Channel ch = getChannel();
        if (ch != null) return ch.getName();
        String nick = getNick();
        if (nick != null) return nick;
        return null;
    }
