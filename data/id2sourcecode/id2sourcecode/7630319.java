    private byte[] encodeWithSPEEX(byte[] buff) {
        if (speexEnc == null) {
            speexEnc = new SpeexEncoder();
            speexEnc.init(band, quality, (int) format.getSampleRate(), format.getChannels());
            speexEnc.getEncoder().setComplexity(2);
        }
        if (bigendian) switchEndianness(buff);
        byte[] out;
        synchronized (speexEnc) {
            speexEnc.processData(buff, 0, buff.length);
            out = new byte[speexEnc.getProcessedDataByteSize()];
            speexEnc.getProcessedData(out, 0);
        }
        return out;
    }
