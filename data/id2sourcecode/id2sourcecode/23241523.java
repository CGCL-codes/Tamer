    public void setWantedTtl(long ms) {
        this.wantedTtl = ms;
        this.timeToExpire = this.getTimeToExpire(this.feed.getChannel());
    }
