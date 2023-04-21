    protected void writeBody(CDROutputPacket os) {
        this.readerId.write(os);
        this.writerId.write(os);
        this.writerSN.write(os);
        if (super.getFlagAt(3)) {
            this.khp.write(os);
            this.khs.write(os);
            ;
        }
        if (super.getFlagAt(4)) {
            this.si.write(os);
        }
        if (super.getFlagAt(1)) {
            this.inlineQoS.write(os);
        }
        if (super.getFlagAt(2)) {
            this.serializedData.write(os);
        }
    }
