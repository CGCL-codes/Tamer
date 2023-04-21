    private void readHeader() throws OpenStegoException {
        boolean[][][][] oldBitRead = null;
        this.dataHeader = new LSBDataHeader(this, this.config);
        this.channelBitsUsed = this.dataHeader.getChannelBitsUsed();
        if (this.channelBitsUsed > 1) {
            oldBitRead = this.bitRead;
            this.bitRead = new boolean[this.imgWidth][this.imgHeight][3][this.channelBitsUsed];
            for (int i = 0; i < this.imgWidth; i++) {
                for (int j = 0; j < this.imgHeight; j++) {
                    this.bitRead[i][j][0][0] = oldBitRead[i][j][0][0];
                    this.bitRead[i][j][1][0] = oldBitRead[i][j][1][0];
                    this.bitRead[i][j][2][0] = oldBitRead[i][j][2][0];
                    for (int k = 1; k < this.channelBitsUsed; k++) {
                        this.bitRead[i][j][0][k] = false;
                        this.bitRead[i][j][1][k] = false;
                        this.bitRead[i][j][2][k] = false;
                    }
                }
            }
        }
    }
