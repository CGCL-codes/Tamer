    public static void byte2floatInterleaved(byte[] input, int inByteOffset, float[] output, int outOffset, int frameCount, AudioFormat format) {
        byte2floatGeneric(input, inByteOffset, format.getFrameSize() / format.getChannels(), output, outOffset, frameCount * format.getChannels(), format);
    }
