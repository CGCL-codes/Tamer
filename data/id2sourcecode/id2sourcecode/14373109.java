    protected void writeBody(CDROutputPacket os) {
        this.readerId.write(os);
        this.writerId.write(os);
        this.firstSN.write(os);
        this.lastSN.write(os);
        this.count.write(os);
    }
