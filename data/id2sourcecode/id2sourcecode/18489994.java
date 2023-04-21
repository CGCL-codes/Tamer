    public void init() {
        nChannels = getFormat().getChannels();
        audioDataIn = ByteBuffer.allocate(BLOCKSIZE * nChannels);
        audioDataIn.order(ByteOrder.LITTLE_ENDIAN);
        shortDataIn = audioDataIn.asShortBuffer();
        audioDataOut = ByteBuffer.allocate(BLOCKSIZE * nChannels);
        audioDataOut.order(ByteOrder.LITTLE_ENDIAN);
        shortDataOut = audioDataOut.asShortBuffer();
        for (int stage = 0; stage < STAGE_COUNT; stage++) {
            c[stage] = Math.cos((.5 - (2. * stage + 1) / (4 * STAGE_COUNT)) * Math.PI);
        }
        for (int band = 0; band < BAND_COUNT; band++) {
            double fC = FIRST_CENTER_FREQUENCY * Math.pow(2, band);
            double fL = fC / Math.sqrt(2);
            double fU = fC * Math.sqrt(2);
            if (fU > MAX_UPPER_FREQ) {
                fU = MAX_UPPER_FREQ;
            }
            double fB = fU - fL;
            double wB = 2 * Math.PI / SAMPLING_RATE * fB;
            double wU = 2 * Math.PI / SAMPLING_RATE * fU;
            double wL = 2 * Math.PI / SAMPLING_RATE * fL;
            double wM = 2 * Math.atan(Math.sqrt(Math.tan(wU / 2) * Math.tan(wL / 2)));
            KBase[band] = Math.tan(wB / 2);
            c0[band] = Math.cos(wM);
            setGain(band, 1.0);
        }
    }
