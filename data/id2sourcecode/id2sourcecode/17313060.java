    @Override
    public String toString() {
        return this.getClass().getName() + " SourceSession: " + this.getSourceSession() + " Direction: " + (this.getDir() == Direction.UP ? "UP" : "DOWN") + " Channel: " + this.getChannel().getChannelID();
    }
