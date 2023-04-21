    private boolean isChannelsOK(AudioFormat format, boolean notSpecifiedOK) {
        int channels = format.getChannels();
        return (notSpecifiedOK && (channels == AudioSystem.NOT_SPECIFIED)) || (channels == 1) || (channels == 2);
    }
