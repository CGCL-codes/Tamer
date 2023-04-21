    public void run() {
        runInit();
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        SpectFrame bufFr = null;
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        Param decayBase;
        Param decay;
        Modulator decayMod = null;
        float srcAmp, srcPhase;
        float srcAmp2, srcPhase2;
        double destReal, destImg;
        int divisor = 0;
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
            decayBase = Param.transform(new Param(pr.para[PR_DECAY].val * SpectStream.framesToMillis(runInStream, 1) / 1000, pr.para[PR_DECAY].unit), Param.ABS_AMP, ampRef, runInStream);
            decay = decayBase;
            if (pr.bool[PR_DECAYMOD]) {
                decayMod = new Modulator(decayBase, pr.para[PR_DECAYMODDEPTH], pr.envl[PR_DECAYMODENV], runInStream);
            }
            bufFr = new SpectFrame(runInStream.chanNum, runInStream.bands);
            SpectFrame.clear(bufFr);
            runSlotsReady();
            mainLoop: while (!threadDead) {
                if (pr.bool[PR_DECAYMOD]) {
                    decay = decayMod.calc();
                }
                for (boolean readDone = false; (readDone == false) && !threadDead; ) {
                    try {
                        runInFr = runInSlot.readFrame();
                        readDone = true;
                    } catch (InterruptedException e) {
                    } catch (EOFException e) {
                        break mainLoop;
                    }
                    runCheckPause();
                }
                if (threadDead) break mainLoop;
                switch(pr.intg[PR_MODE]) {
                    case PR_MODE_SMEAR:
                        switch(pr.intg[PR_DRYAPPLY]) {
                            case PR_APPLY_NONE:
                                for (int ch = 0; ch < runOutStream.chanNum; ch++) {
                                    for (int band = 0; band < runOutStream.bands; band++) {
                                        srcAmp = bufFr.data[ch][(band << 1) + SpectFrame.AMP] / (float) decay.val;
                                        srcPhase = bufFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        destImg = srcAmp * Math.sin(srcPhase);
                                        destReal = srcAmp * Math.cos(srcPhase);
                                        srcAmp = runInFr.data[ch][(band << 1) + SpectFrame.AMP];
                                        srcPhase = runInFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        destImg += srcAmp * Math.sin(srcPhase);
                                        destReal += srcAmp * Math.cos(srcPhase);
                                        bufFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                                        bufFr.data[ch][(band << 1) + SpectFrame.PHASE] = (float) Math.atan2(destImg, destReal);
                                    }
                                }
                                runOutFr = new SpectFrame(bufFr);
                                break;
                            case PR_APPLY_SUB:
                                runOutFr = runOutStream.allocFrame();
                                for (int ch = 0; ch < runOutStream.chanNum; ch++) {
                                    for (int band = 0; band < runOutStream.bands; band++) {
                                        srcAmp = bufFr.data[ch][(band << 1) + SpectFrame.AMP] / (float) decay.val;
                                        srcPhase = bufFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = srcAmp;
                                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = srcPhase;
                                        destImg = srcAmp * Math.sin(srcPhase);
                                        destReal = srcAmp * Math.cos(srcPhase);
                                        srcAmp = runInFr.data[ch][(band << 1) + SpectFrame.AMP];
                                        srcPhase = runInFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        destImg += srcAmp * Math.sin(srcPhase);
                                        destReal += srcAmp * Math.cos(srcPhase);
                                        bufFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                                        bufFr.data[ch][(band << 1) + SpectFrame.PHASE] = (float) Math.atan2(destImg, destReal);
                                    }
                                }
                                break;
                            case PR_APPLY_THRESH:
                                runOutFr = runOutStream.allocFrame();
                                for (int ch = 0; ch < runOutStream.chanNum; ch++) {
                                    for (int band = 0; band < runOutStream.bands; band++) {
                                        srcAmp = bufFr.data[ch][(band << 1) + SpectFrame.AMP] / (float) decay.val;
                                        srcPhase = bufFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        destImg = srcAmp * Math.sin(srcPhase);
                                        destReal = srcAmp * Math.cos(srcPhase);
                                        srcAmp2 = runInFr.data[ch][(band << 1) + SpectFrame.AMP];
                                        srcPhase2 = runInFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        destImg += srcAmp2 * Math.sin(srcPhase2);
                                        destReal += srcAmp2 * Math.cos(srcPhase2);
                                        bufFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                                        bufFr.data[ch][(band << 1) + SpectFrame.PHASE] = (float) Math.atan2(destImg, destReal);
                                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = Math.max(0.0f, srcAmp - srcAmp2);
                                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = srcPhase;
                                    }
                                }
                                break;
                        }
                        break;
                    case PR_MODE_FREEZE:
                        divisor++;
                        runOutFr = runOutStream.allocFrame();
                        switch(pr.intg[PR_DRYAPPLY]) {
                            case PR_APPLY_NONE:
                                for (int ch = 0; ch < runOutStream.chanNum; ch++) {
                                    for (int band = 0; band < runOutStream.bands; band++) {
                                        srcAmp = runInFr.data[ch][(band << 1) + SpectFrame.AMP];
                                        srcPhase = runInFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        bufFr.data[ch][band << 1] += srcAmp * Math.sin(srcPhase);
                                        bufFr.data[ch][(band << 1) + 1] += srcAmp * Math.cos(srcPhase);
                                        destImg = bufFr.data[ch][band << 1] / divisor;
                                        destReal = bufFr.data[ch][(band << 1) + 1] / divisor;
                                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = (float) Math.atan2(destImg, destReal);
                                    }
                                }
                                break;
                            case PR_APPLY_SUB:
                                for (int ch = 0; ch < runOutStream.chanNum; ch++) {
                                    for (int band = 0; band < runOutStream.bands; band++) {
                                        srcAmp = runInFr.data[ch][(band << 1) + SpectFrame.AMP];
                                        srcPhase = runInFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        destImg = srcAmp * Math.sin(srcPhase);
                                        destReal = srcAmp * Math.cos(srcPhase);
                                        bufFr.data[ch][band << 1] += destImg;
                                        bufFr.data[ch][(band << 1) + 1] += destReal;
                                        destImg -= bufFr.data[ch][band << 1] / divisor;
                                        destReal -= bufFr.data[ch][(band << 1) + 1] / divisor;
                                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = (float) Math.atan2(destImg, destReal);
                                    }
                                }
                                break;
                            case PR_APPLY_THRESH:
                                for (int ch = 0; ch < runOutStream.chanNum; ch++) {
                                    for (int band = 0; band < runOutStream.bands; band++) {
                                        srcAmp = runInFr.data[ch][(band << 1) + SpectFrame.AMP];
                                        srcPhase = runInFr.data[ch][(band << 1) + SpectFrame.PHASE];
                                        bufFr.data[ch][band << 1] += srcAmp * Math.sin(srcPhase);
                                        bufFr.data[ch][(band << 1) + 1] += srcAmp * Math.cos(srcPhase);
                                        destImg = bufFr.data[ch][band << 1] / divisor;
                                        destReal = bufFr.data[ch][(band << 1) + 1] / divisor;
                                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = Math.max(0.0f, srcAmp - (float) Math.sqrt(destImg * destImg + destReal * destReal));
                                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = srcPhase;
                                    }
                                }
                                break;
                        }
                        break;
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
