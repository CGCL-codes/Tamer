    public void run() {
        runInit();
        int i, j, ch, numFinished;
        float f1;
        SpectStreamSlot runInSlot[] = new SpectStreamSlot[NUM_INPUT];
        SpectStreamSlot runOutSlot;
        SpectStream runInStream[] = new SpectStream[NUM_INPUT];
        SpectStream runOutStream;
        SpectFrame runInFr[] = new SpectFrame[NUM_INPUT];
        SpectFrame runOutFr;
        int runChanStart[] = new int[NUM_INPUT];
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        double gain = 1.0;
        int numIn = 0;
        int oldReadDone;
        int readable;
        int chanNum = 0;
        float srcData[];
        float destData[];
        int srcCh;
        int dataLen;
        float srcAmp, srcPhase;
        double srcReal, srcImg;
        boolean wantsRect;
        boolean wantsInt;
        int intFrame[][] = null;
        topLevel: try {
            for (i = 0; (i < NUM_INPUT) && !threadDead; i++) {
                try {
                    runInFr[numIn] = null;
                    runInSlot[numIn] = (SpectStreamSlot) slots.elementAt(SLOT_INPUT + i);
                    if (runInSlot[numIn].getLinked() != null) {
                        runInStream[numIn] = runInSlot[numIn].getDescr();
                        if (pr.intg[PR_OPERATION] == PR_OPERATION_COLLECT) {
                            runChanStart[numIn] = chanNum;
                            chanNum += runInStream[numIn].chanNum;
                        } else {
                            runChanStart[numIn] = 0;
                            chanNum = Math.max(chanNum, runInStream[numIn].chanNum);
                        }
                        numIn++;
                    }
                } catch (InterruptedException e) {
                    i--;
                }
                runCheckPause();
            }
            if (numIn == 0) runStop();
            if (threadDead) break topLevel;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream[0]);
            runOutStream.setChannels(chanNum);
            runOutSlot.initWriter(runOutStream);
            if (pr.bool[PR_ADJUSTGAIN]) {
                gain = (float) Param.transform(pr.para[PR_GAIN], Param.ABS_AMP, ampRef, runInStream[0]).val;
                if (Math.abs(gain - 1.0f) < 0.01f) {
                    gain = 1.0f;
                }
            }
            ;
            wantsRect = (pr.intg[PR_DATAFORM] == PR_DATAFORM_RECT);
            wantsInt = (pr.intg[PR_OPERATION] == PR_OPERATION_AND) || (pr.intg[PR_OPERATION] == PR_OPERATION_OR) || (pr.intg[PR_OPERATION] == PR_OPERATION_XOR);
            if (wantsInt) {
                intFrame = new int[runOutStream.chanNum][runOutStream.bands << 1];
            }
            runSlotsReady();
            mainLoop: while (!threadDead) {
                for (int readDone = 0; (readDone < numIn) && !threadDead; ) {
                    oldReadDone = readDone;
                    for (i = 0, numFinished = 0; i < numIn; i++) {
                        try {
                            if (runInFr[i] == null) {
                                readable = runInStream[i].framesReadable();
                                if (readable > 0) {
                                    runInFr[i] = runInSlot[i].readFrame();
                                    readDone++;
                                } else if (readable < 0) {
                                    numFinished++;
                                    if (numFinished == numIn) break mainLoop;
                                    runInFr[i] = runInStream[i].allocFrame();
                                    for (ch = 0; ch < runInFr[i].data.length; ch++) {
                                        srcData = runInFr[i].data[ch];
                                        for (j = 0; j < srcData.length; j++) {
                                            srcData[j] = 0.0f;
                                        }
                                    }
                                    readDone++;
                                }
                            }
                        } catch (InterruptedException e) {
                            break mainLoop;
                        }
                        runCheckPause();
                    }
                    if (oldReadDone == readDone) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                        runCheckPause();
                    }
                }
                if (threadDead) break mainLoop;
                runOutFr = runOutStream.allocFrame();
                if (pr.intg[PR_OPERATION] == PR_OPERATION_COLLECT) {
                    for (i = 0; i < numIn; i++) {
                        for (ch = 0; ch < runInFr[i].data.length; ch++) {
                            srcData = runInFr[i].data[ch];
                            destData = runOutFr.data[runChanStart[i] + ch];
                            System.arraycopy(srcData, 0, destData, 0, Math.min(destData.length, srcData.length));
                            for (j = srcData.length; j < destData.length; j++) {
                                destData[j] = 0.0f;
                            }
                        }
                    }
                } else {
                    srcCh = runInFr[0].data.length;
                    if (!wantsRect) {
                        if (!wantsInt) {
                            for (ch = 0; ch < runOutFr.data.length; ch++) {
                                srcData = runInFr[0].data[ch % srcCh];
                                destData = runOutFr.data[ch];
                                System.arraycopy(srcData, 0, destData, 0, Math.min(destData.length, srcData.length));
                                for (j = srcData.length; j < destData.length; j++) {
                                    destData[j] = 0.0f;
                                }
                            }
                        } else {
                            for (ch = 0; ch < runOutFr.data.length; ch++) {
                                srcData = runInFr[0].data[ch % srcCh];
                                dataLen = Math.min(intFrame[ch].length, srcData.length);
                                for (j = 0; j < dataLen; j += 2) {
                                    intFrame[ch][j + SpectFrame.AMP] = (int) (srcData[j + SpectFrame.AMP] * 0x7FFFFFFF);
                                    intFrame[ch][j + SpectFrame.PHASE] = (int) (((srcData[j + SpectFrame.PHASE] + Math.PI) % Constants.PI2) * 0x145F306D);
                                }
                                for (j = srcData.length; j < intFrame[ch].length; j += 2) {
                                    intFrame[ch][j + SpectFrame.AMP] = 0;
                                    intFrame[ch][j + SpectFrame.PHASE] = 0x3FFFFFFF;
                                }
                            }
                        }
                    } else {
                        if (!wantsInt) {
                            for (ch = 0; ch < runOutFr.data.length; ch++) {
                                srcData = runInFr[0].data[ch % srcCh];
                                destData = runOutFr.data[ch];
                                dataLen = Math.min(destData.length, srcData.length);
                                for (j = 0; j < dataLen; j += 2) {
                                    srcAmp = srcData[j + SpectFrame.AMP];
                                    srcPhase = srcData[j + SpectFrame.PHASE];
                                    destData[j] = srcAmp * (float) Math.cos(srcPhase);
                                    destData[j + 1] = srcAmp * (float) Math.sin(srcPhase);
                                }
                                for (j = srcData.length; j < destData.length; j++) {
                                    destData[j] = 0.0f;
                                }
                            }
                        } else {
                            for (ch = 0; ch < runOutFr.data.length; ch++) {
                                srcData = runInFr[0].data[ch % srcCh];
                                dataLen = Math.min(intFrame[ch].length, srcData.length);
                                for (j = 0; j < dataLen; j += 2) {
                                    srcAmp = srcData[j + SpectFrame.AMP] * 0x3FFFFFFF;
                                    srcPhase = srcData[j + SpectFrame.PHASE];
                                    intFrame[ch][j] = (int) (srcAmp * Math.cos(srcPhase));
                                    intFrame[ch][j + 1] = (int) (srcAmp * Math.sin(srcPhase));
                                }
                                for (j = srcData.length; j < intFrame[ch].length; j++) {
                                    intFrame[ch][j] = 0;
                                }
                            }
                        }
                    }
                    for (i = 1; i < numIn; i++) {
                        srcCh = runInFr[i].data.length;
                        for (ch = 0; ch < runOutFr.data.length; ch++) {
                            srcData = runInFr[i].data[ch % srcCh];
                            destData = runOutFr.data[ch];
                            dataLen = Math.min(destData.length, srcData.length);
                            switch(pr.intg[PR_OPERATION]) {
                                case PR_OPERATION_ADD:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] += srcData[j + SpectFrame.AMP];
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j] += srcData[j];
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP];
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                destData[j] += srcAmp * Math.cos(srcPhase);
                                                destData[j + 1] += srcAmp * Math.sin(srcPhase);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_SUBSTRACT:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                f1 = destData[j + SpectFrame.AMP] - srcData[j + SpectFrame.AMP];
                                                destData[j + SpectFrame.AMP] = Math.max(0.0f, f1);
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j++) {
                                                destData[j] -= srcData[j];
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP];
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                destData[j] -= srcAmp * Math.cos(srcPhase);
                                                destData[j + 1] -= srcAmp * Math.sin(srcPhase);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_MULTIPLY:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] *= srcData[j + SpectFrame.AMP];
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j++) {
                                                destData[j] *= srcData[j];
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP];
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                destData[j] *= srcAmp * Math.cos(srcPhase);
                                                destData[j + 1] *= srcAmp * Math.sin(srcPhase);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_DIVIDE:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] = (float) (destData[j + SpectFrame.AMP] / Math.max(srcData[j + SpectFrame.AMP], 1.0e-6)) % 1.0f;
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] = (float) (destData[j + SpectFrame.AMP] / Math.max(srcData[j + SpectFrame.AMP], 1.0e-6)) % 1.0f;
                                                destData[j + SpectFrame.PHASE] = (float) ((destData[j + SpectFrame.PHASE] / Math.max(srcData[j + SpectFrame.PHASE] + Math.PI, 1.0e-6)) % Constants.PI2);
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP];
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                destData[j] = (float) (destData[j] / Math.max(srcAmp * (1.0 + Math.cos(srcPhase)), 1.0e-6)) % 1.0f;
                                                destData[j + 1] = (float) (destData[j + 1] / Math.max(srcAmp * (1.0 + Math.sin(srcPhase)), 1.0e-6)) % 1.0f;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_AND:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                intFrame[ch][j + SpectFrame.AMP] &= (int) (srcData[j + SpectFrame.AMP] * 0x7FFFFFFF);
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j += 2) {
                                                intFrame[ch][j + SpectFrame.AMP] &= (int) (srcData[j + SpectFrame.AMP] * 0x7FFFFFFF);
                                                intFrame[ch][j + SpectFrame.PHASE] &= (int) (srcData[j + SpectFrame.PHASE] * 0x145F306D);
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP] * 0x3FFFFFFF;
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                intFrame[ch][j] &= (int) (srcAmp * Math.cos(srcPhase));
                                                intFrame[ch][j + 1] &= (int) (srcAmp * Math.sin(srcPhase));
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_OR:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                intFrame[ch][j + SpectFrame.AMP] |= (int) (srcData[j + SpectFrame.AMP] * 0x7FFFFFFF);
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j += 2) {
                                                intFrame[ch][j + SpectFrame.AMP] |= (int) (srcData[j + SpectFrame.AMP] * 0x7FFFFFFF);
                                                intFrame[ch][j + SpectFrame.PHASE] |= (int) (srcData[j + SpectFrame.PHASE] * 0x145F306D);
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP] * 0x3FFFFFFF;
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                intFrame[ch][j] |= (int) (srcAmp * Math.cos(srcPhase));
                                                intFrame[ch][j + 1] |= (int) (srcAmp * Math.sin(srcPhase));
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_XOR:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                intFrame[ch][j + SpectFrame.AMP] ^= (int) (srcData[j + SpectFrame.AMP] * 0x7FFFFFFF);
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j += 2) {
                                                intFrame[ch][j + SpectFrame.AMP] ^= (int) (srcData[j + SpectFrame.AMP] * 0x7FFFFFFF);
                                                intFrame[ch][j + SpectFrame.PHASE] ^= (int) (srcData[j + SpectFrame.PHASE] * 0x145F306D);
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP] * 0x3FFFFFFF;
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                intFrame[ch][j] ^= (int) (srcAmp * Math.cos(srcPhase));
                                                intFrame[ch][j + 1] ^= (int) (srcAmp * Math.sin(srcPhase));
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_EXP:
                                    break;
                                case PR_OPERATION_LOG:
                                    break;
                                case PR_OPERATION_MODULO:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] %= Math.max(srcData[j + SpectFrame.AMP], 1.0e-6);
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] %= Math.max(srcData[j + SpectFrame.AMP], 1.0e-6);
                                                destData[j + SpectFrame.PHASE] %= Math.max(srcData[j + SpectFrame.PHASE] + Math.PI, 1.0e-6);
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP];
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                destData[j] %= Math.max(srcAmp * (1.0 + Math.cos(srcPhase)) / 2, 1.0e-6);
                                                destData[j + 1] %= Math.max(srcAmp * (1.0 + Math.sin(srcPhase)) / 2, 1.0e-6);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case PR_OPERATION_ATAN:
                                    switch(pr.intg[PR_DATAFORM]) {
                                        case PR_DATAFORM_AMP:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] = 1.0f - (float) (Math.atan2(destData[j + SpectFrame.AMP], srcData[j + SpectFrame.AMP]) / Constants.PIH);
                                            }
                                            break;
                                        case PR_DATAFORM_POLAR:
                                            for (j = 0; j < dataLen; j += 2) {
                                                destData[j + SpectFrame.AMP] = 1.0f - (float) (Math.atan2(destData[j + SpectFrame.AMP], srcData[j + SpectFrame.AMP]) / Constants.PIH);
                                                destData[j + SpectFrame.PHASE] = (float) Math.atan2(destData[j + SpectFrame.PHASE], srcData[j + SpectFrame.PHASE]);
                                            }
                                            break;
                                        case PR_DATAFORM_RECT:
                                            for (j = 0; j < dataLen; j += 2) {
                                                srcAmp = srcData[j + SpectFrame.AMP];
                                                srcPhase = srcData[j + SpectFrame.PHASE];
                                                destData[j] = (float) (Math.atan2(destData[j], srcAmp * Math.cos(srcPhase)) / Math.PI);
                                                destData[j + 1] = (float) (Math.atan2(destData[j + 1], srcAmp * Math.sin(srcPhase)) / Math.PI);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (!wantsInt) {
                        if (wantsRect) {
                            for (ch = 0; ch < runOutFr.data.length; ch++) {
                                destData = runOutFr.data[ch];
                                for (j = 0; j < destData.length; j += 2) {
                                    srcReal = destData[j];
                                    srcImg = destData[j + 1];
                                    destData[j + SpectFrame.AMP] = (float) Math.sqrt(srcReal * srcReal + srcImg * srcImg);
                                    destData[j + SpectFrame.PHASE] = (float) Math.atan2(srcImg, srcReal);
                                }
                            }
                        }
                    } else {
                        if (!wantsRect) {
                            for (ch = 0; ch < runOutFr.data.length; ch++) {
                                destData = runOutFr.data[ch];
                                for (j = 0; j < destData.length; j += 2) {
                                    destData[j + SpectFrame.AMP] = (float) intFrame[ch][j + SpectFrame.AMP] / 0x7FFFFFFF;
                                    destData[j + SpectFrame.PHASE] = (float) ((double) intFrame[ch][j + SpectFrame.PHASE] / 0x145F306D - Math.PI);
                                }
                            }
                        } else {
                            for (ch = 0; ch < runOutFr.data.length; ch++) {
                                destData = runOutFr.data[ch];
                                for (j = 0; j < destData.length; j += 2) {
                                    srcReal = (double) intFrame[ch][j] / 0x3FFFFFFF;
                                    srcImg = (double) intFrame[ch][j + 1] / 0x3FFFFFFF;
                                    destData[j + SpectFrame.AMP] = (float) Math.sqrt(srcReal * srcReal + srcImg * srcImg);
                                    destData[j + SpectFrame.PHASE] = (float) Math.atan2(srcImg, srcReal);
                                }
                            }
                        }
                    }
                }
                if (gain != 1.0f) {
                    for (ch = 0; ch < runOutFr.data.length; ch++) {
                        destData = runOutFr.data[ch];
                        for (j = 0; j < destData.length; j += 2) {
                            destData[j + SpectFrame.AMP] *= gain;
                        }
                    }
                }
                for (i = 0; i < numIn; i++) {
                    runInSlot[i].freeFrame(runInFr[i]);
                    runInFr[i] = null;
                }
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
            for (i = 0; i < numIn; i++) {
                runInStream[i].closeReader();
            }
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
