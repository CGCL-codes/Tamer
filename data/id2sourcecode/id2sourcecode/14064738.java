    @Override
    public boolean equals(Object arg0) {
        if (arg0 == this) return true;
        if (arg0 instanceof IRCChannelBean) {
            IRCChannelBean other = (IRCChannelBean) arg0;
            return (other.getChannelname().equals(this.getChannelname()));
        }
        return false;
    }
