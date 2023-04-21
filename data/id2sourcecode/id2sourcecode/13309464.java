    public void run() {
        runInit();
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot[] = new SpectStreamSlot[NUM_OUTPUT];
        SpectStream runInStream = null;
        SpectStream runOutStream[] = new SpectStream[NUM_OUTPUT];
        SpectFrame runInFr = null;
        SpectFrame runOutFr[] = new SpectFrame[NUM_OUTPUT];
        int runSlotNum[] = new int[NUM_OUTPUT];
        int runChanNum[] = new int[NUM_OUTPUT];
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        double normBase;
        Param gainBase[] = new Param[NUM_OUTPUT];
        float gain[] = new float[NUM_OUTPUT];
        Modulator gainMod[] = new Modulator[NUM_OUTPUT];
        double sumGain;
        int numOut = 0;
        int oldWriteDone;
        int writeable;
        int chanNum;
        float srcData[];
        float destData[];
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
            for (int i = 0; i < NUM_OUTPUT; i++) {
                runOutSlot[numOut] = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT + i);
                if (runOutSlot[numOut].getLinked() != null) {
                    runOutStream[numOut] = new SpectStream(runInStream);
                    if (pr.intg[PR_CHANNELS] == PR_CHANNELS_SINGLE) {
                        runOutStream[numOut].setChannels(1);
                        runChanNum[numOut] = numOut % runInStream.chanNum;
                    } else {
                        runChanNum[numOut] = 0;
                    }
                    runOutSlot[numOut].initWriter(runOutStream[numOut]);
                    runSlotNum[numOut] = i;
                    numOut++;
                }
            }
            normBase = Param.transform(pr.para[PR_NORMGAIN], Param.ABS_AMP, ampRef, runInStream).val;
            for (int i = 0; i < numOut; i++) {
                gainBase[i] = Param.transform(pr.para[PR_GAIN + runSlotNum[i]], Param.ABS_AMP, ampRef, runInStream);
                if (pr.bool[PR_GAINMOD + runSlotNum[i]]) {
                    gainMod[i] = new Modulator(gainBase[i], pr.para[PR_GAINMODDEPTH + runSlotNum[i]], pr.envl[PR_GAINMODENV + runSlotNum[i]], runInStream);
                }
            }
            if (pr.intg[PR_CHANNELS] == PR_CHANNELS_SINGLE) {
                chanNum = 1;
            } else {
                chanNum = runInStream.chanNum;
            }
            runSlotsReady();
            mainLoop: while (!threadDead && (numOut > 0)) {
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
                sumGain = 0.0;
                for (int i = 0; i < numOut; i++) {
                    if (pr.bool[PR_GAINMOD + runSlotNum[i]]) {
                        gain[i] = (float) gainMod[i].calc().val;
                    } else {
                        gain[i] = (float) gainBase[i].val;
                    }
                    sumGain += gain[i];
                }
                if (pr.bool[PR_NORMALIZE]) {
                    for (int i = 0; i < numOut; i++) {
                        gain[i] *= (float) (normBase / sumGain);
                    }
                }
                calcFrame: for (int i = 0; i < numOut; i++) {
                    if ((Math.abs(gain[i] - 1.0f) < 0.01f) && (pr.intg[PR_CHANNELS] == PR_CHANNELS_ALL)) {
                        runOutFr[i] = new SpectFrame(runInFr);
                        gain[i] = 1.0f;
                    } else {
                        for (int j = 0; j < i; j++) {
                            if ((Math.abs(gain[i] - gain[j]) < 0.01f) && ((pr.intg[PR_CHANNELS] == PR_CHANNELS_ALL) || ((i % runInStream.chanNum) == (j % runInStream.chanNum)))) {
                                runOutFr[i] = new SpectFrame(runOutFr[j]);
                                continue calcFrame;
                            }
                        }
                        runOutFr[i] = runOutStream[i].allocFrame();
                        for (int ch = 0; ch < chanNum; ch++) {
                            srcData = runInFr.data[runChanNum[i] + ch];
                            destData = runOutFr[i].data[ch];
                            if (gain[i] < 0.01f) {
                                for (int j = 0; j < destData.length; j++) {
                                    destData[j] = 0.0f;
                                }
                            } else {
                                System.arraycopy(srcData, 0, destData, 0, srcData.length);
                                if (Math.abs(gain[i] - 1.0f) >= 0.01f) {
                                    for (int j = 0; j < destData.length; j += 2) {
                                        destData[j + SpectFrame.AMP] *= gain[i];
                                    }
                                } else {
                                    gain[i] = 1.0f;
                                }
                            }
                        }
                    }
                }
                runFrameDone(runInSlot, runInFr);
                runInSlot.freeFrame(runInFr);
                for (int writeDone = 0; (writeDone < numOut) && !threadDead; ) {
                    oldWriteDone = writeDone;
                    for (int i = 0; i < numOut; i++) {
                        try {
                            if (runOutFr[i] != null) {
                                writeable = runOutStream[i].framesWriteable();
                                if (writeable > 0) {
                                    runOutSlot[i].writeFrame(runOutFr[i]);
                                    writeDone++;
                                    runOutStream[i].freeFrame(runOutFr[i]);
                                    runOutFr[i] = null;
                                } else if (writeable < 0) {
                                    writeDone++;
                                    runFrameDone(runOutSlot[i], runOutFr[i]);
                                    runOutStream[i].freeFrame(runOutFr[i]);
                                    for (int j = i + 1; j < numOut; j++) {
                                        runOutSlot[j] = runOutSlot[j - 1];
                                        runOutStream[j] = runOutStream[j - 1];
                                        runOutFr[j] = runOutFr[j - 1];
                                        runSlotNum[j] = runSlotNum[j - 1];
                                        runChanNum[j] = runChanNum[j - 1];
                                        gainBase[j] = gainBase[j - 1];
                                        gain[j] = gain[j - 1];
                                        gainMod[j] = gainMod[j - 1];
                                    }
                                    numOut--;
                                    i--;
                                }
                            }
                        } catch (InterruptedException e) {
                            break mainLoop;
                        }
                        runCheckPause();
                    }
                    if (oldWriteDone == writeDone) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                        runCheckPause();
                    }
                }
            }
            runInStream.closeReader();
            for (int i = 0; i < numOut; i++) {
                runOutStream[i].closeWriter();
            }
        } catch (IOException e) {
            runQuit(e);
            return;
        } catch (SlotAlreadyConnectedException e) {
            runQuit(e);
            return;
        }
        runQuit(null);
    }
