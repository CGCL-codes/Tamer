    protected void writeBody(CDROutputPacket os) {
        this.readerId.write(os);
        this.writerId.write(os);
        this.gapStart.write(os);
        this.gapList.write(os);
    }
