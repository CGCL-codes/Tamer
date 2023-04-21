    public void run() {
        runInit();
        int ch, i, j;
        double d1, d2;
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        float gain;
        int srcBands;
        float[] fftBuf1, fftBuf2, convBuf1;
        double fltFreq, fltShift;
        int fftLength, skip;
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
            srcBands = runInStream.bands;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutSlot.initWriter(runOutStream);
            gain = 2 * (float) Param.transform(pr.para[PR_GAIN], Param.ABS_AMP, ampRef, runInStream).val;
            fltFreq = runInStream.smpRate * 0.245;
            fltShift = Constants.PI2 * 0.245;
            fltBox = new FilterBox();
            fltBox.filterType = FilterBox.FLT_LOWPASS;
            fltBox.cutOff = new Param(fltFreq, Param.ABS_HZ);
            tmpStream = new AudioFileDescr();
            tmpStream.rate = runInStream.smpRate;
            fltLength = fltBox.calcLength(tmpStream, FIRDesignerDlg.QUAL_VERYGOOD);
            skip = fltLength.x;
            i = fltLength.x + fltLength.y;
            j = i + srcBands - 1;
            for (fftLength = 2; fftLength < j; fftLength <<= 1) ;
            fftBuf1 = new float[fftLength << 1];
            fftBuf2 = new float[fftLength << 1];
            Util.clear(fftBuf1);
            fltBox.calcIR(tmpStream, FIRDesignerDlg.QUAL_VERYGOOD, Filter.WIN_BLACKMAN, fftBuf1, fltLength);
            for (i = fftLength - 1, j = fftBuf1.length - 1; i >= 0; i--) {
                d1 = -fltShift * i;
                fftBuf1[j--] = (float) (fftBuf1[i] * Math.sin(d1));
                fftBuf1[j--] = (float) (fftBuf1[i] * Math.cos(d1));
            }
            Fourier.complexTransform(fftBuf1, fftLength, Fourier.FORWARD);
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
                    Fourier.polar2Rect(convBuf1, 0, fftBuf2, 0, srcBands << 1);
                    for (i = srcBands << 1; i < fftBuf2.length; i++) {
                        fftBuf2[i] = 0.0f;
                    }
                    Fourier.complexTransform(fftBuf2, fftLength, Fourier.FORWARD);
                    Fourier.complexMult(fftBuf1, 0, fftBuf2, 0, fftBuf2, 0, fftLength << 1);
                    Fourier.complexTransform(fftBuf2, fftLength, Fourier.INVERSE);
                    convBuf1 = runOutFr.data[ch];
                    for (i = 0, j = (skip << 1); i < (srcBands << 1); ) {
                        d1 = fftBuf2[j++];
                        d2 = fftBuf2[j++];
                        convBuf1[i++] = gain * (float) Math.sqrt(d1 * d1 + d2 * d2);
                        convBuf1[i++] = 0.0f;
                    }
                    if (pr.bool[PR_INVERT]) {
                        for (i = 0, j = 0; i < srcBands; i++, j += 2) {
                            convBuf1[j] = 1.0f - convBuf1[j];
                        }
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
