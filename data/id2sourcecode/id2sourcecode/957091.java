    protected void writeBody(CDROutputPacket os) {
        readerId.write(os);
        writerId.write(os);
        writerSN.write(os);
        if (super.getFlagAt(1)) {
            inlineQoS.write(os);
        }
        fragmentStartingNum.write(os);
        os.write_short(fragmentsInSubmessage);
        os.write_short(fragmentSize);
        os.write_long(sampleSize);
        serializedData.write(os);
    }
