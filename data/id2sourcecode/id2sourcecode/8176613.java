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
        int srcBands, lpCoeffNum;
        float loFreq, hiFreq, freqSpacing;
        float[] lpCoeffBuf, convBuf1, convBuf2, convBuf3;
        float[][] lpBuf;
        int loBand, hiBand;
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
            freqSpacing = (runInStream.hiFreq - runInStream.loFreq) / runInStream.bands;
            srcBands = runInStream.bands;
            lpBuf = new float[2][srcBands];
            loFreq = Math.min(runInStream.smpRate / 2, (float) pr.para[PR_LOFREQ].val);
            hiFreq = Math.max(0.5f, Math.min(runInStream.smpRate / 2, (float) pr.para[PR_HIFREQ].val));
            if (loFreq > hiFreq) {
                f1 = loFreq;
                loFreq = hiFreq;
                hiFreq = f1;
            }
            loBand = (int) ((loFreq - runInStream.loFreq) / freqSpacing + 0.5f);
            hiBand = (int) ((hiFreq - runInStream.loFreq) / freqSpacing + 0.5f);
            lpCoeffNum = Math.min(64, hiBand - loBand);
            lpCoeffBuf = new float[lpCoeffNum];
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
                    convBuf2 = lpBuf[0];
                    convBuf3 = lpBuf[1];
                    for (i = 0, j = 0; j < srcBands; ) {
                        f2 = convBuf1[i++];
                        f1 = convBuf1[i++];
                        convBuf2[j] = f2 * (float) Math.cos(f1);
                        convBuf3[j++] = f2 * (float) Math.sin(f1);
                    }
                    for (i = 0; i < 2; i++) {
                        convBuf1 = lpBuf[i];
                        lpCoeffs(convBuf1, loBand, hiBand - loBand, lpCoeffBuf, lpCoeffNum);
                        linearPrediction2(convBuf1, loBand, hiBand - loBand, lpCoeffBuf, lpCoeffNum, convBuf1, hiBand, srcBands - hiBand);
                    }
                    convBuf1 = runOutFr.data[ch];
                    convBuf2 = lpBuf[0];
                    convBuf3 = lpBuf[1];
                    for (i = 0, j = 0; j < srcBands; ) {
                        f1 = convBuf2[j];
                        f2 = convBuf3[j++];
                        convBuf1[i++] = complexAbs(f1, f2);
                        convBuf1[i++] = (float) Math.atan2(f2, f1);
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
