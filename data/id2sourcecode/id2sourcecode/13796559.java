    private void readHeader() throws OpenStegoException {
        this.dataHeader = new LSBDataHeader(this, this.config);
        this.channelBitsUsed = this.dataHeader.getChannelBitsUsed();
        if (this.currBit != 0) {
            this.currBit = 0;
            this.x++;
            if (this.x == this.imgWidth) {
                this.x = 0;
                this.y++;
            }
        }
    }
