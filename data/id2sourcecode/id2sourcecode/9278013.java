    public float readFloat() {
        try {
            float readSample;
            if (ch) {
                int left = WavUtils.bgtolt(data.readShort());
                int right = WavUtils.bgtolt(data.readShort());
                readSample = (float) ((left + right) / 2 * WavUtils.MIN_POSITIVE_SHORT);
                log.debug("[] stereo:  " + readSample);
            } else {
                Short readShort = new Short(data.readShort());
                float sample1 = (float) (WavUtils.bgtolt(readShort.shortValue()) * WavUtils.MIN_POSITIVE_SHORT);
                readShort = new Short(data.readShort());
                float sample2 = (float) (WavUtils.bgtolt(readShort.shortValue()) * WavUtils.MIN_POSITIVE_SHORT);
                readSample = (sample1 + sample2) / 2;
            }
            return readSample;
        } catch (IOException e) {
            eof = true;
        }
        return 0;
    }
