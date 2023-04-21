    public void run() {
        runInit();
        int ch, i, j;
        int fltSmpPerCrossing, fltCrossings, fltLen;
        float flt[], fltD[], filter[][], fltWin, fltRolloff;
        float fltGain;
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        int shrinkFactor;
        int srcBands, destBands;
        float rsmpFactor;
        float[] srcBuf, destBuf, rectBuf;
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
            shrinkFactor = 2 << pr.intg[PR_FACTOR];
            srcBands = runInStream.bands;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            if (pr.intg[PR_MODE] == MODE_SHRINK) {
                destBands = (srcBands - 1) / shrinkFactor + 1;
                rsmpFactor = (float) destBands / (float) srcBands;
                runOutStream.smpPerFrame /= shrinkFactor;
            } else {
                destBands = (srcBands - 1) * shrinkFactor + 1;
                rsmpFactor = (float) destBands / (float) srcBands;
                runOutStream.smpPerFrame *= shrinkFactor;
            }
            runOutStream.bands = destBands;
            runOutSlot.initWriter(runOutStream);
            fltSmpPerCrossing = 4096;
            fltCrossings = 5;
            fltRolloff = 0.70f;
            fltWin = 6.5f;
            fltSmpPerCrossing = 4096;
            fltLen = (int) ((float) (fltSmpPerCrossing * fltCrossings) / fltRolloff + 0.5f);
            flt = new float[fltLen];
            fltD = null;
            fltGain = Filter.createAntiAliasFilter(flt, fltD, fltLen, fltSmpPerCrossing, fltRolloff, fltWin);
            filter = new float[3][];
            filter[0] = flt;
            filter[1] = fltD;
            filter[2] = new float[2];
            filter[2][0] = fltSmpPerCrossing;
            filter[2][1] = fltGain;
            rectBuf = new float[Math.max(srcBands, destBands) << 1];
            srcBuf = new float[srcBands + 1];
            destBuf = new float[destBands + 1];
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
                    Fourier.polar2Rect(runInFr.data[ch], 0, rectBuf, 0, srcBands << 1);
                    for (i = 0, j = 0; i < srcBands; i++, j += 2) {
                        srcBuf[i] = rectBuf[j];
                    }
                    Filter.resample(srcBuf, 0.0, destBuf, 0, destBands, rsmpFactor, filter);
                    for (i = 0, j = 0; i < destBands; i++, j += 2) {
                        rectBuf[j] = destBuf[i];
                    }
                    for (i = 0, j = 1; i < srcBands; i++, j += 2) {
                        srcBuf[i] = rectBuf[j];
                    }
                    Filter.resample(srcBuf, 0.0, destBuf, 0, destBands, rsmpFactor, filter);
                    for (i = 0, j = 1; i < destBands; i++, j += 2) {
                        rectBuf[j] = destBuf[i];
                    }
                    Fourier.rect2Polar(rectBuf, 0, runOutFr.data[ch], 0, destBands << 1);
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
