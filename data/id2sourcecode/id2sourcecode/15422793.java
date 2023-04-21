    @Override
    public String getValue() {
        try {
            return this.getAudioHeader().getChannels();
        } catch (final Exception ex) {
            MetadataChannels.LOGGER.warn("[MetadataChannels - getValue] cannot get Channels info");
        }
        return null;
    }
