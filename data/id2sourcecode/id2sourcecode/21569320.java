    private static byte[] decodeWithSPEEX(byte[] buff) {
        if (speexDec == null) {
            speexDec = new SpeexDecoder();
            speexDec.init(Recorder.band, (int) Recorder.format.getSampleRate(), Recorder.format.getChannels(), true);
        }
        try {
            speexDec.processData(buff, 0, buff.length);
        } catch (Exception ex) {
        }
        byte[] ret = new byte[speexDec.getProcessedDataByteSize()];
        speexDec.getProcessedData(ret, 0);
        if (Recorder.bigendian) Recorder.switchEndianness(ret);
        return ret;
    }
