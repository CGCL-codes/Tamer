    public static void float2byteInterleaved(float[] input, int inOffset, byte[] output, int outByteOffset, int frameCount, AudioFormat format, float ditherBits) {
        float2byteGeneric(input, inOffset, output, outByteOffset, format.getFrameSize() / format.getChannels(), frameCount * format.getChannels(), format, ditherBits);
    }
