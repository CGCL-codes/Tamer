    public void run() {
        runInit();
        int ch, i;
        float f1, f2, maxGain;
        double exp;
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        int srcBands, fftSize, fullFFTsize, winSize, winHalf;
        float[] fftBuf, convBuf1, convBuf2, win;
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
            winSize = srcBands - 1;
            winHalf = winSize >> 1;
            win = Filter.createFullWindow(winSize, Filter.WIN_BLACKMAN);
            fftSize = srcBands - 1;
            fullFFTsize = fftSize << 1;
            fftBuf = new float[fullFFTsize + 2];
            exp = (Param.transform(pr.para[PR_CONTRAST], Param.ABS_AMP, ampRef, null)).val - 1.0;
            maxGain = (float) (Param.transform(pr.para[PR_MAXBOOST], Param.ABS_AMP, ampRef, null)).val;
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
                    convBuf1 = runInFr.data[ch];
                    convBuf2 = runOutFr.data[ch];
                    fftBuf[0] = 1.0f;
                    fftBuf[1] = 0.0f;
                    for (i = 2; i < fullFFTsize; ) {
                        f2 = (convBuf1[i - 2] + convBuf1[i + 2]);
                        if (f2 > 0.0f) {
                            f1 = (float) Math.min(maxGain, Math.pow(2.0f * convBuf1[i] / f2, exp));
                        } else {
                            if (convBuf1[i] == 0.0f) {
                                f1 = 1.0f;
                            } else {
                                f1 = maxGain;
                            }
                        }
                        fftBuf[i++] = f1;
                        fftBuf[i++] = 0.0f;
                    }
                    fftBuf[i++] = 1.0f;
                    fftBuf[i++] = 0.0f;
                    Fourier.realTransform(fftBuf, fullFFTsize, Fourier.INVERSE);
                    Util.mult(win, winHalf, fftBuf, 0, winHalf);
                    for (i = winHalf; i < fullFFTsize - winHalf; ) {
                        fftBuf[i++] = 0.0f;
                    }
                    Util.mult(win, 0, fftBuf, i, winHalf);
                    Fourier.realTransform(fftBuf, fullFFTsize, Fourier.FORWARD);
                    for (i = 0; i <= fullFFTsize; ) {
                        convBuf2[i] = convBuf1[i] * fftBuf[i];
                        i++;
                        convBuf2[i] = convBuf1[i];
                        i++;
                    }
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
