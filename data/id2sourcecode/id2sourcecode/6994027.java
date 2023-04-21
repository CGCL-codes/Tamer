    public Integer getTrackChannel(Track t) throws NoChannelAssignedException {
        try {
            ShortMessage sm = ProjectHelper.firstShortMessage(t);
            return sm.getChannel();
        } catch (MidiMessageNotFoundException e) {
            throw new NoChannelAssignedException("E_NO_CHANNEL_ASSIGNED");
        }
    }
