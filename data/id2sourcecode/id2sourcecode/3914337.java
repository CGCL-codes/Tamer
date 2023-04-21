    public ChannelSetData(final int bufferId, final ChannelChanges changes) {
        super(ID, "*C9ee#".length() + (6 * changes.size()));
        this.bufferId = bufferId;
        this.changes = changes;
        Util.validate("changesCount", changes.size(), 1, MAX_CHANGES);
        set2(3, bufferId);
        for (int i = 0; i < changes.size(); i++) {
            ChannelChange change = changes.get(i);
            set4(5 + (i * 6), change.getChannelId());
            set2(5 + (i * 6) + 4, change.getDmxValue());
        }
    }
