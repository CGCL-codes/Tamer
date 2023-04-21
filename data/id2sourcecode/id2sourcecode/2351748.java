    public static void byte2float(byte[] input, int inByteOffset, Object[] output, int outOffset, int frameCount, AudioFormat format, boolean allowAddChannel) {
        int channels = format.getChannels();
        if (!allowAddChannel && channels > output.length) {
            channels = output.length;
        }
        if (output.length < channels) {
            throw new ArrayIndexOutOfBoundsException("too few channel output array");
        }
        for (int channel = 0; channel < channels; channel++) {
            float[] data = (float[]) output[channel];
            if (data.length < frameCount + outOffset) {
                data = new float[frameCount + outOffset];
                output[channel] = data;
            }
            byte2floatGeneric(input, inByteOffset, format.getFrameSize(), data, outOffset, frameCount, format);
            inByteOffset += format.getFrameSize() / format.getChannels();
        }
    }
