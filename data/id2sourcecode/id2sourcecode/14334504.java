    public byte[] getChannelStatus() {
        if (!fttInitialized) {
            return null;
        }
        return fttApi.readChannelStatus();
    }
