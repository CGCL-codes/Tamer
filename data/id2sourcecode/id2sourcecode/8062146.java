    public static void float2byteNonInterleaved(List input, int inOffset, byte[] output, int outByteOffset, int frameCount, AudioFormat format, float ditherBits) {
        for (int channel = 0; channel < format.getChannels(); channel++) {
            float[] data = (float[]) input.get(channel);
            float2byteGeneric(data, inOffset, output, outByteOffset, format.getFrameSize(), frameCount, format, ditherBits);
            outByteOffset += format.getFrameSize() / format.getChannels();
        }
    }
