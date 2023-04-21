    public static void byte2float(byte[] input, int inByteOffset, List<float[]> output, int outOffset, int frameCount, AudioFormat format, boolean allowAddChannel) {
        int channels = format.getChannels();
        if (!allowAddChannel && channels > output.size()) {
            channels = output.size();
        }
        for (int channel = 0; channel < channels; channel++) {
            float[] data;
            if (output.size() < channel) {
                data = new float[frameCount + outOffset];
                output.add(data);
            } else {
                data = output.get(channel);
                if (data.length < frameCount + outOffset) {
                    data = new float[frameCount + outOffset];
                    output.set(channel, data);
                }
            }
            byte2floatGeneric(input, inByteOffset, format.getFrameSize(), data, outOffset, frameCount, format);
            inByteOffset += format.getFrameSize() / format.getChannels();
        }
    }
