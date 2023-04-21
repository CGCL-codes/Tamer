    @Override
    public boolean equals(Object arg0) {
        if (arg0 == this) return true;
        if (arg0 instanceof IRCJoinBean) {
            IRCJoinBean other = (IRCJoinBean) arg0;
            if (other.getChannel() == null) throw new IllegalArgumentException("channel must not be null");
            if (other.getUser() == null) throw new IllegalArgumentException("user must not be null");
            if (this.getChannel() == null) throw new IllegalArgumentException("channel must not be null");
            if (this.getUser() == null) throw new IllegalArgumentException("user must not be null");
            return (other.getChannel().equals(this.getChannel()) && other.getUser().equals(this.getUser()));
        }
        return false;
    }
