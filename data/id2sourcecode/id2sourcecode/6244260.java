    public TempChannel getTempChannel(int channel) {
        Iterator it = this.tempChannels.iterator();
        while (it.hasNext()) {
            TempChannel tempChannel = (TempChannel) it.next();
            if (tempChannel.getChannel() == channel) {
                return tempChannel;
            }
        }
        TempChannel tempChannel = new TempChannel(channel);
        this.tempChannels.add(tempChannel);
        return tempChannel;
    }
