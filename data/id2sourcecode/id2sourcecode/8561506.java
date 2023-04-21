    protected void writeBody(CDROutputPacket os) {
        this.readerId.write(os);
        this.writerId.write(os);
        this.writerSN.write(os);
        if (super.getFlagAt(2)) {
            this.khp.write(os);
            this.khs.write(os);
        }
        if (super.getFlagAt(1)) {
            this.inlineQoS.write(os);
        }
        this.fsn.write(os);
        this.fis.write(os);
        this.fsize.write(os);
        this.sampleSize.write(os);
        this.serializedData.write(os);
    }
