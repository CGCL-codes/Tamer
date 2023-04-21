    @Override
    public String toString() {
        return "[IRCJoin " + getUser().getNickname() + "(" + getUser().getId() + ")|" + getChannel().getChannelname() + " (" + modeline + ")]";
    }
