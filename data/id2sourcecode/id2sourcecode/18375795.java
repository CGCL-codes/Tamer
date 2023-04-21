    public StripChannelBinding getFirstChannel() {
        if (getChannelBindings().size() == 0) return null;
        return getChannelBindings().get(0);
    }
