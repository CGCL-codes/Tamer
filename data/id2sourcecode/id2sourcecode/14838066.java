    public void insertChannel(int index, boolean silent, boolean lazy) {
        int physSize = channels.size();
        int virtSize = getChannelCount();
        float[] newChannel = null;
        if (physSize > virtSize) {
            for (int ch = virtSize; ch < physSize; ch++) {
                float[] thisChannel = (float[]) channels.get(ch);
                if ((lazy && thisChannel.length >= getSampleCount()) || (!lazy && thisChannel.length == getSampleCount())) {
                    newChannel = thisChannel;
                    channels.remove(ch);
                    break;
                }
            }
        }
        if (newChannel == null) {
            newChannel = new float[getSampleCount()];
        }
        channels.add(index, newChannel);
        this.channelCount++;
        if (silent) {
            makeSilence(index);
        }
    }
