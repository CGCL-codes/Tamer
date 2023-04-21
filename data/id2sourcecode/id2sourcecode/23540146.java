    protected void writeBody(CDROutputPacket os) {
        readerId.write(os);
        writerId.write(os);
        sns.write(os);
        count.write(os);
    }
