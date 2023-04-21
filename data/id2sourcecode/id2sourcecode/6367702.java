    public int mix(AudioBuffer destBuffer, AudioBuffer sourceBuffer, float[] gain) {
        boolean doMix = destBuffer != sourceBuffer;
        int snc = sourceBuffer.getChannelCount();
        int dnc = destBuffer.getChannelCount();
        if (dnc > 4 && snc != dnc) dnc = 4;
        int ns = destBuffer.getSampleCount();
        float g;
        float k = (float) (snc) / dnc;
        float[] in;
        float[] out;
        for (int i = 0; i < dnc; i++) {
            g = gain[i] * k;
            in = sourceBuffer.getChannel(i % snc);
            out = destBuffer.getChannel(i);
            if (doMix) {
                for (int s = 0; s < ns; s++) {
                    out[s] += in[s] * g;
                }
            } else {
                for (int s = 0; s < ns; s++) {
                    out[s] = in[s] * g;
                }
            }
        }
        int ret = 1;
        if (!doMix) ret |= 2;
        return ret;
    }
