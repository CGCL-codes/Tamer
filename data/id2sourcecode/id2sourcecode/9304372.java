    public void run() {
        runInit();
        final SpectStreamSlot runInSlot;
        final SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        double srcFreq;
        float srcBand;
        final int[] srcFloorBands, srcCeilBands;
        final float[] srcFloorWeights, srcCeilWeights;
        int srcFloorBand;
        int srcCeilBand;
        float srcFloorWeight;
        float srcCeilWeight;
        float srcAmp, srcPhase;
        double destReal, destImg;
        final float loFreq, hiFreq, freqSpacing;
        final boolean randPhase = pr.bool[PR_RANDPHASE];
        final float pi = (float) Math.PI;
        int startBand = 0, stopBand;
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
            srcFloorBands = new int[runInStream.bands + 1];
            srcFloorWeights = new float[runInStream.bands + 1];
            srcCeilBands = new int[runInStream.bands + 1];
            srcCeilWeights = new float[runInStream.bands + 1];
            stopBand = runInStream.bands;
            freqSpacing = (runInStream.hiFreq - runInStream.loFreq) / runInStream.bands;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutSlot.initWriter(runOutStream);
            loFreq = (float) pr.para[PR_LOFREQ].val;
            hiFreq = (float) pr.para[PR_HIFREQ].val;
            final double expLinFactor = (runInStream.bands - 1) / Math.log(hiFreq / loFreq);
            for (int band = 0; band <= runInStream.bands; band++) {
                srcFreq = runInStream.loFreq + band * freqSpacing;
                srcBand = (float) (Math.log(srcFreq / loFreq) * expLinFactor + 1);
                srcFloorBands[band] = (int) Math.floor(srcBand);
                srcFloorWeights[band] = 1.0f - (srcBand - srcFloorBands[band]);
            }
            fixLp: for (int band = 0; band < runInStream.bands; band++) {
                srcFloorBand = srcFloorBands[band];
                srcCeilBand = srcFloorBands[band + 1];
                srcFloorWeight = srcFloorWeights[band];
                srcCeilWeight = 1.0f - (srcFloorWeights[band + 1]);
                if (srcFloorBand < 0) {
                    srcFloorBand = 0;
                    if (srcFloorBand < srcCeilBand) {
                        srcFloorWeight = 1.0f;
                    } else {
                        startBand = band + 1;
                        continue fixLp;
                    }
                }
                if (srcCeilBand >= runInStream.bands) {
                    srcCeilBand = runInStream.bands - 1;
                    if (srcCeilBand > srcFloorBand) {
                        srcCeilWeight = 1.0f;
                    } else {
                        stopBand = band;
                        break fixLp;
                    }
                }
                if (srcFloorBand == srcCeilBand) {
                    srcFloorWeight = srcCeilWeight - (1.0f - srcFloorWeight);
                    srcCeilWeight = 0.0f;
                }
                srcFloorBands[band] = srcFloorBand;
                srcFloorWeights[band] = srcFloorWeight;
                srcCeilBands[band] = srcCeilBand;
                srcCeilWeights[band] = srcCeilWeight;
            }
            runSlotsReady();
            final Random rnd = new Random();
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
                for (int band = 0; band < startBand; band++) {
                    for (int ch = 0; ch < runInStream.chanNum; ch++) {
                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = 0.0f;
                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = 0.0f;
                    }
                }
                for (int band = startBand; band < stopBand; band++) {
                    srcFloorBand = srcFloorBands[band];
                    srcCeilBand = srcCeilBands[band];
                    srcFloorWeight = srcFloorWeights[band];
                    srcCeilWeight = srcCeilWeights[band];
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
                        if (srcCeilWeight > 0) {
                            srcAmp = runInFr.data[ch][(srcCeilBand << 1) + SpectFrame.AMP];
                            srcPhase = runInFr.data[ch][(srcCeilBand << 1) + SpectFrame.PHASE];
                            destImg += srcAmp * Math.sin(srcPhase) * srcCeilWeight;
                            destReal += srcAmp * Math.cos(srcPhase) * srcCeilWeight;
                        }
                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = randPhase ? rnd.nextFloat() * pi : (float) Math.atan2(destImg, destReal);
                    }
                }
                for (int band = stopBand; band < runInStream.bands; band++) {
                    for (int ch = 0; ch < runInStream.chanNum; ch++) {
                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = 0.0f;
                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = 0.0f;
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
