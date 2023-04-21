    public void run() {
        runInit();
        int ch, i, j;
        float f1, f2;
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        int srcBands, fftSize, fullFFTsize, complexFFTsize;
        float[] fftBuf, convBuf1, convBuf2;
        int clr, cli, crr, cri, ccr, cci, car, cai;
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
            fftSize = srcBands - 1;
            fullFFTsize = fftSize << 1;
            complexFFTsize = fullFFTsize << 1;
            fftBuf = new float[complexFFTsize];
            crr = pr.intg[PR_CRR] - 1;
            cri = pr.intg[PR_CRI] - 1;
            clr = pr.intg[PR_CLR] - 1;
            cli = pr.intg[PR_CLI] - 1;
            ccr = pr.intg[PR_CCR] - 1;
            cci = pr.intg[PR_CCI] - 1;
            car = pr.intg[PR_CAR] - 1;
            cai = pr.intg[PR_CAI] - 1;
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
                    for (i = 0; i <= fullFFTsize; ) {
                        fftBuf[i] = (float) Math.log(Math.max(1.0e-24, convBuf1[i]));
                        i++;
                        fftBuf[i] = convBuf1[i];
                        i++;
                    }
                    for (i = fullFFTsize + 2, j = fullFFTsize - 2; i < complexFFTsize; j -= 2) {
                        fftBuf[i++] = fftBuf[j];
                        fftBuf[i++] = -fftBuf[j + 1];
                    }
                    Fourier.complexTransform(fftBuf, fullFFTsize, Fourier.INVERSE);
                    fftBuf[0] *= crr;
                    fftBuf[1] *= cri;
                    for (i = 2, j = complexFFTsize - 2; i < fullFFTsize; i += 2, j -= 2) {
                        f1 = fftBuf[i];
                        f2 = fftBuf[j];
                        fftBuf[i] = crr * f1 + ccr * f2;
                        fftBuf[j] = clr * f2 + car * f1;
                        f1 = fftBuf[i + 1];
                        f2 = fftBuf[j + 1];
                        fftBuf[i + 1] = cri * f1 + cci * f2;
                        fftBuf[j + 1] = cli * f2 + cai * f1;
                    }
                    fftBuf[i++] *= ccr + clr;
                    fftBuf[i++] *= cci + cli;
                    Fourier.complexTransform(fftBuf, fullFFTsize, Fourier.FORWARD);
                    for (i = 0; i <= fullFFTsize; ) {
                        convBuf2[i] = (float) Math.exp(fftBuf[i]);
                        i++;
                        convBuf2[i] = fftBuf[i];
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
