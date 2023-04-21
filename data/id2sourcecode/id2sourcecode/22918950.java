    protected void doWork() {
        chunkPtr = -1;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(" Interrupted before I even started !! ");
            e.printStackTrace();
            return;
        }
        dt = chunksize / Fs;
        fftCQ = new FFTConstantQ(Fs, minF, maxF, binsPerOctave, thresh, spread);
        int fftsize = fftCQ.getFFTSize();
        double freq[] = fftCQ.freqs;
        synchronized (this) {
            freqArray = new float[freq.length];
            for (int i = 0; i < freq.length; i++) {
                freqArray[i] = (float) freq[i];
            }
            System.out.println(" fftsize/chunkSIze = " + fftsize + "/" + chunksize);
            nBin = fftCQ.getNumberOfOutputBands();
            size = new Dimension(sizeInChunks, nBin);
            dPhaseFreqHz = new float[sizeInChunks][nBin];
            magnArray = new float[sizeInChunks][nBin];
            phaseArray = new float[sizeInChunks][nBin];
        }
        double twoPI = 2 * Math.PI;
        double dPhaRef[] = new double[nBin];
        for (int i = 0; i < nBin; i++) {
            dPhaRef[i] = (twoPI * freq[i] * dt);
        }
        double fftOut[] = new double[nBin * 2];
        double fftIn[] = new double[fftsize];
        double input[] = new double[fftsize];
        try {
            reader.seekFrame(0, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double testF = minF * 2;
        int ch = reader.getChannels();
        int nRead = 0;
        AudioBuffer buffer = new AudioBuffer("TEMP", ch, chunksize, 44100);
        chunkPtr = -fftsize / chunksize / 2;
        int extraChunks = -chunkPtr;
        notifySizeObservers();
        chunkStartInSamples = 0;
        do {
            if (Thread.interrupted()) {
                return;
            }
            if (fftsize != chunksize) {
                for (int i = 0; i < fftsize - chunksize; i++) input[i] = input[i + chunksize];
            }
            buffer.makeSilence();
            reader.processAudio(buffer);
            nRead += chunksize;
            float left[] = buffer.getChannel(0);
            for (int i = fftsize - chunksize, j = 0; i < fftsize; i++, j++) {
                input[i] = left[j];
            }
            if (chunkPtr < 0) {
                chunkPtr++;
                chunkStartInSamples += chunksize;
                continue;
            }
            for (int i = 0; i < fftsize; i++) {
                fftIn[i] = input[i];
            }
            fftCQ.calc(fftIn, fftOut);
            for (int i = 0; i < nBin; i++) {
                double real = fftOut[2 * i];
                double imag = fftOut[2 * i + 1];
                magnArray[chunkPtr][i] = (float) Math.sqrt(real * real + imag * imag);
                phaseArray[chunkPtr][i] = (float) Math.atan2(imag, real);
                double phaLast;
                if (chunkPtr > 0) {
                    phaLast = phaseArray[chunkPtr - 1][i];
                } else {
                    phaLast = 0.0;
                }
                double dpha = phaseArray[chunkPtr][i] - phaLast;
                dpha = -((dPhaRef[i] - dpha + Math.PI + twoPI) % twoPI - Math.PI);
                dPhaseFreqHz[chunkPtr][i] = (float) (freq[i] + dpha / twoPI / dt);
            }
            chunkPtr++;
            if (chunkPtr % 50 == 0) {
                notifyMoreDataObservers();
            }
        } while (chunkPtr < sizeInChunks);
        System.out.println(" DATA BUILT ");
        notifyMoreDataObservers();
    }
