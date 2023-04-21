    public static void switchEndianness(byte[] samples) {
        for (int i = 0; i < samples.length; i += 2) {
            byte tmp = samples[i];
            samples[i] = samples[i + 1];
            samples[i + 1] = tmp;
        }
    }
