    private void readHeader() throws OpenStegoException {
        dataHeader = new DataHeader(this, config);
        this.channelBitsUsed = dataHeader.getChannelBitsUsed();
        if (currBit != 0) {
            currBit = 0;
            x++;
            if (x == imgWidth) {
                x = 0;
                y++;
            }
        }
    }
