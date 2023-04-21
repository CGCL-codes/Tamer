    public static void byte2float(byte[] input, int inByteOffset, List<float[]> output, int outOffset, int frameCount, AudioFormat format) {
        for (int channel = 0; channel < format.getChannels(); channel++) {
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
