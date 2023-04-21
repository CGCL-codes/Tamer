    public String getChannelDescription(Integer channel) {
        if (channel < 0 || channel > this.numChannels.intValue()) throw new IllegalArgumentException("Illegal channel value: " + channel);
        if (this.channels.size() == 0) return ""; else return this.channels.get(channel).getDescription();
    }
