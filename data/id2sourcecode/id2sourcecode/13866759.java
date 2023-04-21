    public int process(Buffer inBuf, Buffer outBuf) {
        if (inBuf.isEOM()) {
            outBuf.setLength(0);
            outBuf.setEOM(true);
            return BUFFER_PROCESSED_OK;
        }
        byte hdr[] = (byte[]) inBuf.getData();
        int offset = inBuf.getOffset();
        int rate = ((hdr[offset + 1] & 0xff) << 16) | ((hdr[offset + 2] & 0xff) << 8) | (hdr[offset + 3] & 0xff);
        int sizeInBits = hdr[offset + 4];
        int channels = hdr[offset + 5];
        int endian = hdr[offset + 6];
        int signed = hdr[offset + 7];
        if ((int) outFormat.getSampleRate() != rate || outFormat.getSampleSizeInBits() != sizeInBits || outFormat.getChannels() != channels || outFormat.getEndian() != endian || outFormat.getSigned() != signed) {
            outFormat = new AudioFormat(AudioFormat.LINEAR, rate, sizeInBits, channels, endian, signed);
        }
        Object outData = outBuf.getData();
        outBuf.setData(inBuf.getData());
        inBuf.setData(outData);
        outBuf.setLength(inBuf.getLength() - HDR_SIZE);
        outBuf.setOffset(inBuf.getOffset() + HDR_SIZE);
        outBuf.setFormat(outFormat);
        return BUFFER_PROCESSED_OK;
    }
