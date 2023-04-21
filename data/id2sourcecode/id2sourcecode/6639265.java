    public int hashCode() {
        if (getCommChannel() != null) return (getCommChannel().getChannelName() + getAddress() + getBoardIdentifier()).hashCode(); else return (getAddress() + getBoardIdentifier()).hashCode();
    }
