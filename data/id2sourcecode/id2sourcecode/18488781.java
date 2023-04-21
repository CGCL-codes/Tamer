    public short[] createSamples(byte[] samples) {
        short[] sampleShort = new short[samples.length / 2];
        byte[] temp = new byte[2];
        for (int i = 0, j = 0; i < samples.length; i += 2, j++) {
            temp[0] = samples[i];
            temp[1] = samples[i + 1];
            sampleShort[j] = TypeConverter.byteArrayToShort(temp);
        }
        return sampleShort;
    }
