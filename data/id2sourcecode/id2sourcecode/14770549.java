    @Override
    public int compareTo(Entity other) throws IllegalArgumentException {
        int rv = 0;
        if (other instanceof EpgEvent) {
            EpgEvent e2 = (EpgEvent) other;
            if (getBegin() != null && e2.getBegin() != null) rv = getBegin().compareTo(e2.getBegin());
            if (rv == 0) {
                if (getChannel() != null && e2.getChannel() != null) rv = this.getChannel().compareTo(e2.getChannel()); else rv = duration - e2.getDuration();
            }
            if (rv == 0) rv = super.compareTo(other);
        } else rv = super.compareTo(other);
        return rv;
    }
