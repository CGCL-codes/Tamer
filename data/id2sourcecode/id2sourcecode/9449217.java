    public void run() {
        runInit();
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        Param midBase, shiftBase, hiBase, loBase;
        int phaseFactor;
        boolean recalc = true;
        Param midFreq;
        Param shiftFreq;
        Param hiFreq;
        Param loFreq;
        Modulator midMod = null;
        Modulator shiftMod = null;
        Modulator hiMod = null;
        Modulator loMod = null;
        Param foo;
        double freqSpacing;
        double hiScaling;
        double loScaling;
        double loRange;
        double hiRange;
        double destFreq;
        double srcFreq;
        float srcBand;
        int srcBands[];
        float srcWeights[];
        int srcFloorBand;
        int srcCeilBand;
        float srcFloorWeight;
        float srcCeilWeight;
        float srcAmp, srcPhase;
        double destReal, destImg;
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
            srcBands = new int[runInStream.bands + 1];
            srcWeights = new float[runInStream.bands + 1];
            freqSpacing = (runInStream.hiFreq - runInStream.loFreq) / runInStream.bands;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutSlot.initWriter(runOutStream);
            if (pr.bool[PR_FLIPFREQ]) {
                phaseFactor = -1;
            } else {
                phaseFactor = +1;
            }
            midBase = Param.transform(pr.para[PR_MIDFREQ], Param.ABS_HZ, null, runInStream);
            shiftBase = Param.transform(pr.para[PR_SHIFTFREQ], Param.ABS_HZ, midBase, runInStream);
            hiBase = Param.transform(pr.para[PR_HIFREQ], Param.ABS_HZ, midBase, runInStream);
            loBase = Param.transform(pr.para[PR_LOFREQ], Param.ABS_HZ, midBase, runInStream);
            midFreq = midBase;
            shiftFreq = shiftBase;
            hiFreq = hiBase;
            loFreq = loBase;
            if (pr.bool[PR_MIDMOD]) {
                midMod = new Modulator(midBase, pr.para[PR_MIDMODDEPTH], pr.envl[PR_MIDMODENV], runInStream);
            }
            if (pr.bool[PR_SHIFTMOD]) {
                shiftMod = new Modulator(shiftBase, pr.para[PR_SHIFTMODDEPTH], pr.envl[PR_SHIFTMODENV], runInStream);
            }
            if (pr.bool[PR_HIMOD]) {
                hiMod = new Modulator(hiBase, pr.para[PR_HIMODDEPTH], pr.envl[PR_HIMODENV], runInStream);
            }
            if (pr.bool[PR_LOMOD]) {
                loMod = new Modulator(loBase, pr.para[PR_LOMODDEPTH], pr.envl[PR_LOMODENV], runInStream);
            }
            runSlotsReady();
            mainLoop: while (!threadDead) {
                if (pr.bool[PR_MIDMOD]) {
                    foo = midMod.calc();
                    if (Math.abs(foo.val - midFreq.val) >= 0.1) {
                        midFreq = foo;
                        recalc = true;
                    }
                }
                if (pr.bool[PR_SHIFTMOD]) {
                    foo = shiftMod.calc();
                    if (Math.abs(foo.val - shiftFreq.val) >= 0.1) {
                        shiftFreq = foo;
                        recalc = true;
                    }
                }
                if (pr.bool[PR_HIMOD]) {
                    foo = hiMod.calc();
                    if (Math.abs(foo.val - hiFreq.val) >= 0.1) {
                        hiFreq = foo;
                        recalc = true;
                    }
                }
                if (pr.bool[PR_LOMOD]) {
                    foo = loMod.calc();
                    if (Math.abs(foo.val - loFreq.val) >= 0.1) {
                        loFreq = foo;
                        recalc = true;
                    }
                }
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
                if (recalc) {
                    loRange = Math.max(1, midFreq.val - loFreq.val);
                    hiRange = Math.max(1, hiFreq.val - midFreq.val);
                    hiScaling = loRange / hiRange;
                    loScaling = hiRange / loRange;
                    if (pr.bool[PR_FLIPFREQ]) {
                        for (int band = 0; band <= runInStream.bands; band++) {
                            destFreq = band * freqSpacing + runOutStream.loFreq - shiftFreq.val;
                            if (destFreq >= midFreq.val) {
                                srcFreq = midFreq.val - (destFreq - midFreq.val) * hiScaling;
                            } else {
                                srcFreq = (midFreq.val - destFreq) * loScaling + midFreq.val;
                            }
                            srcBand = (float) ((srcFreq - runInStream.loFreq) / freqSpacing);
                            srcBands[band] = (int) Math.floor(srcBand);
                            srcWeights[band] = srcBand - srcBands[band];
                        }
                    } else {
                        for (int band = 0; band <= runInStream.bands; band++) {
                            destFreq = band * freqSpacing + runOutStream.loFreq + shiftFreq.val;
                            if (destFreq >= midFreq.val) {
                                srcFreq = (destFreq - midFreq.val) * loScaling + midFreq.val;
                            } else {
                                srcFreq = midFreq.val - (midFreq.val - destFreq) * hiScaling;
                            }
                            srcBand = (float) ((srcFreq - runInStream.loFreq) / freqSpacing);
                            srcBands[band] = (int) Math.floor(srcBand);
                            srcWeights[band] = srcBand - srcBands[band];
                        }
                    }
                    recalc = false;
                }
                bandLp: for (int band = 0; band < runInStream.bands; band++) {
                    srcFloorBand = srcBands[band];
                    srcCeilBand = srcBands[band + 1];
                    if (srcFloorBand > srcCeilBand) {
                        srcFloorBand = srcBands[band + 1];
                        srcCeilBand = srcBands[band];
                        srcFloorWeight = 1.0f - srcWeights[band + 1];
                        srcCeilWeight = srcWeights[band];
                    } else {
                        srcFloorWeight = 1.0f - srcWeights[band];
                        srcCeilWeight = srcWeights[band + 1];
                    }
                    if (srcFloorBand < 0) {
                        srcFloorBand = 0;
                        if (srcFloorBand < srcCeilBand) {
                            srcFloorWeight = 1.0f;
                        } else {
                            for (int ch = 0; ch < runInStream.chanNum; ch++) {
                                runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = 0.0f;
                                runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = 0.0f;
                            }
                            continue bandLp;
                        }
                    }
                    if (srcCeilBand >= runInStream.bands) {
                        srcCeilBand = runInStream.bands - 1;
                        if (srcCeilBand > srcFloorBand) {
                            srcCeilWeight = 1.0f;
                        } else {
                            for (int ch = 0; ch < runInStream.chanNum; ch++) {
                                runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = 0.0f;
                                runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = 0.0f;
                            }
                            continue bandLp;
                        }
                    }
                    if (srcFloorBand == srcCeilBand) {
                        srcFloorWeight = srcCeilWeight - (1.0f - srcFloorWeight);
                        srcCeilWeight = 0.0f;
                    }
                    for (int ch = 0; ch < runInStream.chanNum; ch++) {
                        srcAmp = runInFr.data[ch][(srcFloorBand << 1) + SpectFrame.AMP];
                        srcPhase = runInFr.data[ch][(srcFloorBand << 1) + SpectFrame.PHASE];
                        destImg = srcAmp * Math.sin(srcPhase) * srcFloorWeight;
                        destReal = srcAmp * Math.cos(srcPhase) * srcFloorWeight;
                        for (int i = srcFloorBand + 1; i < srcCeilBand; i++) {
                            srcAmp = runInFr.data[ch][(i << 1) + SpectFrame.AMP];
                            srcPhase = runInFr.data[ch][(i << 1) + SpectFrame.PHASE];
                            destImg += srcAmp * Math.sin(srcPhase);
                            destReal += srcAmp * Math.cos(srcPhase);
                        }
                        srcAmp = runInFr.data[ch][(srcCeilBand << 1) + SpectFrame.AMP];
                        srcPhase = runInFr.data[ch][(srcCeilBand << 1) + SpectFrame.PHASE];
                        destImg += srcAmp * Math.sin(srcPhase) * srcCeilWeight;
                        destReal += srcAmp * Math.cos(srcPhase) * srcCeilWeight;
                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = phaseFactor * (float) Math.atan2(destImg, destReal);
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
