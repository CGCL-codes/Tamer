    public CueSceneWrite(final int channelCount, final int cueListNumber, final int cueStepNumber, final ChannelChanges channelChanges) {
        super(ID, TEMPLATE.length() + (BYTES_PER_CHANNEL * channelChanges.size()));
        this.cueListNumber = cueListNumber;
        this.cueStepNumber = cueStepNumber;
        this.channelCount = channelCount;
        this.channelChanges = channelChanges;
        assertChannelCount(channelChanges.size());
        set4(3, cueListNumber);
        set2(7, cueStepNumber);
        set2(9, 0);
        set4(11, channelCount);
        int offset = 15;
        for (ChannelChange change : channelChanges) {
            set4(offset, change.getChannelId());
            set2(offset + 4, change.getDmxValue());
            offset += BYTES_PER_CHANNEL;
        }
    }
