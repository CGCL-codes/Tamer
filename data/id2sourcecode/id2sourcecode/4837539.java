    public ChannelSetData(final int bufferId, final ChannelChange[] changes) {
        super(ID, "*C9ee#".length() + (6 * changes.length));
        this.bufferId = bufferId;
        this.changes = changes;
        Util.validate("changesCount", changes.length, 1, MAX_CHANGES);
        set2(3, bufferId);
        for (int i = 0; i < changes.length; i++) {
            set4(5 + (i * 6), changes[i].getChannelId());
            set2(5 + (i * 6) + 4, changes[i].getDmxValue());
        }
    }
