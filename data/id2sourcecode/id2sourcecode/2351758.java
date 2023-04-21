    public static void float2byte(Object[] input, int inOffset, byte[] output, int outByteOffset, int frameCount, AudioFormat format, float ditherBits) {
        int channels = format.getChannels();
        for (int channel = 0; channel < channels; channel++) {
            float[] data = (float[]) input[channel];
            float2byteGeneric(data, inOffset, output, outByteOffset, format.getFrameSize(), frameCount, format, ditherBits);
            outByteOffset += format.getFrameSize() / format.getChannels();
        }
    }
