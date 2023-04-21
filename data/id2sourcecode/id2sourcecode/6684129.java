    protected void doWork() {
        running = true;
        abortFlag = false;
        chunkPtr = 0;
        int nRead = 0;
        chunkStartInSamples = 0;
        float phaLast[] = phaseArray[0];
        double maxV = 0.0;
        totalFramesRendered = 0;
        do {
            if (abortFlag) break;
            if (fftsize != chunksize) {
                for (int i = 0; i < fftsize - chunksize; i++) input[i] = input[i + chunksize];
            }
            buffer.makeSilence();
            int stat = reader.processAudio(buffer);
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
                fftOut[i] = input[i] * hanning[i];
            }
            fft.calcReal(fftOut, -1);
            for (int i = 0; i < nBin; i++) {
                double real = fftOut[2 * i];
                double imag = fftOut[2 * i + 1];
                magnArray[chunkPtr][i] = (float) Math.sqrt(real * real + imag * imag);
                maxV = Math.max(maxV, magnArray[chunkPtr][i]);
                phaseArray[chunkPtr][i] = (float) Math.atan2(imag, real);
                double dpha = phaseArray[chunkPtr][i] - phaLast[i];
                dpha = -((dPhaRef[i] - dpha + Math.PI + twoPI) % twoPI - Math.PI);
                dPhaseFreqHz[chunkPtr][i] = (float) (freq[i] + dpha / twoPI / dt);
            }
            phaLast = phaseArray[chunkPtr];
            notifyMoreDataObservers(magnArray[chunkPtr]);
            chunkPtr++;
            totalFramesRendered++;
            if (chunkPtr >= sizeInChunks) chunkPtr = 0;
        } while (true);
        running = false;
        abortFlag = false;
        if (abortWaiter != null) abortWaiter.interrupt();
        System.out.println(" ABORTED ");
    }
