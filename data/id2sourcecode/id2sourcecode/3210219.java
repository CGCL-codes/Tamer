    public AudioChannel getChannel() throws VLCException {
        return (AudioChannel.elementForValue(this.getChannelNumber()));
    }
