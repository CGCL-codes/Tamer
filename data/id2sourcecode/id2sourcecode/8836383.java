    public int compare(AudioFormat o1, AudioFormat o2) {
        if (o1 == o2) return 0;
        if (o1 == null) return 1;
        if (o2 == null) return -1;
        AudioFormat f1 = (AudioFormat) o1;
        AudioFormat f2 = (AudioFormat) o2;
        {
            int b1 = f1.getSampleSizeInBits();
            int b2 = f2.getSampleSizeInBits();
            if (b1 != b2) {
                if (b1 == 8) return 1;
                if (b2 == 8) return -1;
            }
        }
        if (o1.equals(o2)) return 0;
        if (f1.matches(f2)) return 0;
        if (desiredAudioFormat.matches(f1)) return -1;
        if (desiredAudioFormat.matches(f2)) return 1;
        {
            float r1 = f1.getSampleRate();
            float r2 = f2.getSampleRate();
            if (r1 != r2) {
                if (r1 == sampleRate || r1 == AudioSystem.NOT_SPECIFIED) return -1;
                if (r2 == sampleRate || r2 == AudioSystem.NOT_SPECIFIED) return 1;
                boolean r1m = ((int) r1 % (int) sampleRate) == 0;
                boolean r2m = ((int) r2 % (int) sampleRate) == 0;
                if (r1m && r2m) {
                    float m1 = r1 / sampleRate;
                    float m2 = r2 / sampleRate;
                    if (m1 < m2) return -1; else if (m2 < m1) return 1;
                } else if (r1m) {
                    return -1;
                } else if (r2m) {
                    return 1;
                }
            }
        }
        {
            AudioFormat.Encoding e1 = f1.getEncoding();
            AudioFormat.Encoding e2 = f2.getEncoding();
            if (e1 != e2) {
                if (e1 == encoding) return -1;
                if (e2 == encoding) return 1;
                if (encoding == AudioFormat.Encoding.PCM_SIGNED || encoding == AudioFormat.Encoding.PCM_UNSIGNED) {
                    if (e1 == AudioFormat.Encoding.PCM_UNSIGNED || e1 == AudioFormat.Encoding.PCM_SIGNED) return -1;
                    if (e2 == AudioFormat.Encoding.PCM_UNSIGNED || e2 == AudioFormat.Encoding.PCM_SIGNED) return 1;
                } else if (encoding == AudioFormat.Encoding.ULAW || encoding == AudioFormat.Encoding.ALAW) {
                    if (e1 == AudioFormat.Encoding.PCM_SIGNED) return -1; else if (e2 == AudioFormat.Encoding.PCM_SIGNED) return 1; else if (e1 == AudioFormat.Encoding.PCM_UNSIGNED) return -1; else if (e2 == AudioFormat.Encoding.PCM_UNSIGNED) return 1; else if (e1 == AudioFormat.Encoding.ULAW || e1 == AudioFormat.Encoding.ALAW) return -1; else if (e2 == AudioFormat.Encoding.ULAW || e2 == AudioFormat.Encoding.ALAW) return 1;
                }
            }
        }
        {
            boolean b1 = f1.isBigEndian();
            boolean b2 = f2.isBigEndian();
            if (b1 != b2) {
                if (b1 == isBigEndian) return -1;
                if (b2 == isBigEndian) return 1;
            }
        }
        {
            int c = conversionCompare(channelFormat(f1, 1), channelFormat(f2, 1));
            if (c != 0) return c;
        }
        {
            int c1 = f1.getChannels();
            int c2 = f2.getChannels();
            if (c1 != c2) {
                if (c1 == channels) return -1;
                if (c2 == channels) return 1;
                if (c1 > channels) {
                    if (c2 > channels) {
                        if (c1 < c2) return -1;
                        if (c1 > c2) return 1;
                    } else return -1;
                } else if (c2 > channels) {
                    return 1;
                } else if (c1 < c2) {
                    return 1;
                } else return -1;
            }
        }
        {
            int h1 = f1.hashCode();
            int h2 = f2.hashCode();
            if (h1 < h2) return -1;
            if (h1 > h2) return 1;
            return 0;
        }
    }
