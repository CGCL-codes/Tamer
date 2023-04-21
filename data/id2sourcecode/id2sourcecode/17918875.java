    protected void writeBody(CDROutputPacket os) {
        readerId.write(os);
        writerId.write(os);
        writerSN.write(os);
        if (this.getFlagAt(1)) {
            inlineQoS.write(os);
        }
        if (this.getFlagAt(2)) {
            serializedData.write(os);
        }
    }
