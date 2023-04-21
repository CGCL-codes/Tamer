    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TopicEvent && o.hashCode() == hashCode()) {
            return ((TopicEvent) o).getChannel().equals(getChannel());
        }
        return false;
    }
