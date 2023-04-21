    public void run() {
        runInit();
        int ch, i, j;
        float f1, f2, phase1, phase2;
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        int skip, srcBands, srcSize, fftSize, fullFFTsize;
        float loFreq, hiFreq;
        float[] fftBuf, fltBuf;
        AudioFileDescr tmpStream;
        FilterBox fltBox;
        Point fltLength;
        topLevel: try {
            runInSlot = (SpectStreamSlot) slots.elementAt(SLOT_INPUT);
            if (runInSlot.getLinked() == null) {
                runStop();
            }
            for (boolean initDone = false; !initDone && !threadDead; ) {
                try {
                    runInStream = runInSlot.getDescr();
                    initDone = true;
                } catch (InterruptedException e) {
                }
                runCheckPause();
            }
            if (threadDead) break topLevel;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutSlot.initWriter(runOutStream);
            srcBands = runInStream.bands;
            srcSize = srcBands << 1;
            loFreq = Math.min(runInStream.smpRate / 2, (float) pr.para[PR_LOFREQ].val);
            hiFreq = Math.max(0.5f, Math.min(runInStream.smpRate / 2, (float) pr.para[PR_HIFREQ].val));
            if (loFreq > hiFreq) {
                f1 = loFreq;
                loFreq = hiFreq;
                hiFreq = f1;
            }
            fltBox = new FilterBox();
            if (loFreq <= 0.1f) {
                fltBox.filterType = FilterBox.FLT_LOWPASS;
                fltBox.cutOff = new Param(hiFreq, Param.ABS_HZ);
            } else if (hiFreq >= runInStream.smpRate / 2) {
                fltBox.filterType = FilterBox.FLT_HIGHPASS;
                fltBox.cutOff = new Param(loFreq, Param.ABS_HZ);
            } else {
                fltBox.filterType = FilterBox.FLT_BANDPASS;
                fltBox.cutOff = new Param((loFreq + hiFreq) / 2, Param.ABS_HZ);
                fltBox.bandwidth = new Param(hiFreq - loFreq, Param.OFFSET_HZ);
            }
            tmpStream = new AudioFileDescr();
            tmpStream.rate = runInStream.smpRate;
            fltLength = fltBox.calcLength(tmpStream, pr.intg[PR_QUALITY]);
            skip = fltLength.x;
            i = fltLength.x + fltLength.y;
            j = i + srcBands - 1;
            for (fftSize = 2; fftSize < j; fftSize <<= 1) ;
            fullFFTsize = fftSize << 1;
            fftBuf = new float[fullFFTsize];
            fltBuf = new float[fullFFTsize];
            Util.clear(fltBuf);
            fltBox.calcIR(tmpStream, pr.intg[PR_QUALITY], Filter.WIN_BLACKMAN, fltBuf, fltLength);
            for (i = fftSize, j = fullFFTsize; i > 0; ) {
                fltBuf[--j] = 0.0f;
                fltBuf[--j] = (float) fltBuf[--i];
            }
            Util.rotate(fltBuf, fullFFTsize, fftBuf, -(skip << 1));
            Fourier.complexTransform(fltBuf, fftSize, Fourier.FORWARD);
            runSlotsReady();
            mainLoop: while (!threadDead) {
                for (boolean readDone = false; (readDone == false) && !threadDead; ) {
                    try {
                        runInFr = runInSlot.readFrame();
                        readDone = true;
                        runOutFr = runOutStream.allocFrame();
                    } catch (InterruptedException e) {
                    } catch (EOFException e) {
                        break mainLoop;
                    }
                    runCheckPause();
                }
                if (threadDead) break mainLoop;
                for (ch = 0; ch < runOutStream.chanNum; ch++) {
                    System.arraycopy(runInFr.data[ch], 0, fftBuf, 0, srcSize);
                    for (i = 0; i < srcSize; i += 2) {
                        f1 = fftBuf[i];
                        if (f1 > 1.266416555e-14f) {
                            fftBuf[i] = (float) (Math.log(f1));
                        } else {
                            fftBuf[i] = -32f;
                        }
                    }
                    Fourier.unwrapPhases(fftBuf, 0, fftBuf, 0, srcSize);
                    phase1 = fftBuf[1];
                    phase2 = fftBuf[srcSize - 1];
                    f2 = (float) (srcSize - 2);
                    for (i = 0; i < srcSize; ) {
                        f1 = (float) i++ / f2;
                        fftBuf[i++] += phase1 * (f1 - 1.0f) - phase2 * f1;
                    }
                    for (i = srcSize; i < fullFFTsize; ) {
                        fftBuf[i++] = 0.0f;
                    }
                    Fourier.complexTransform(fftBuf, fftSize, Fourier.FORWARD);
                    Fourier.complexMult(fltBuf, 0, fftBuf, 0, fftBuf, 0, fullFFTsize);
                    Fourier.complexTransform(fftBuf, fftSize, Fourier.INVERSE);
                    f2 = (float) (srcSize - 2);
                    for (i = 0; i < srcSize; ) {
                        fftBuf[i] = (float) Math.exp(fftBuf[i]);
                        f1 = (float) i++ / f2;
                        fftBuf[i++] += phase1 * (1.0f - f1) + phase2 * f1;
                    }
                    System.arraycopy(fftBuf, 0, runOutFr.data[ch], 0, srcSize);
                }
                runInSlot.freeFrame(runInFr);
                for (boolean writeDone = false; (writeDone == false) && !threadDead; ) {
                    try {
                        runOutSlot.writeFrame(runOutFr);
                        writeDone = true;
                        runFrameDone(runOutSlot, runOutFr);
                        runOutStream.freeFrame(runOutFr);
                    } catch (InterruptedException e) {
                    }
                    runCheckPause();
                }
            }
            runInStream.closeReader();
            runOutStream.closeWriter();
        } catch (IOException e) {
            runQuit(e);
            return;
        } catch (SlotAlreadyConnectedException e) {
            runQuit(e);
            return;
        }
        runQuit(null);
    }
