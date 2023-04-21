    public void run() {
        runInit();
        int i, j, k, m, off, len, ch;
        double d1, d2, d3, d4, d5, d6;
        float f1;
        String runFileName;
        AudioFile runFile = null;
        int runStartFrame;
        int runFrames;
        int runInLength;
        int runFramesRead = 0;
        SpectStreamSlot runOutSlot;
        AudioFileDescr runInStream;
        SpectStream tmpStream;
        SpectStream runOutStream;
        SpectFrame runOutFr;
        Param inLengthP;
        double startShift;
        double outLength;
        int winSize = 131072 >> pr.intg[PR_FFTLENGTH];
        int winHalf = winSize >> 1;
        int winStep = winSize / overlaps[pr.intg[PR_OVERLAP]];
        int fftLength, fullFFTlength, bands;
        float[] win;
        int inLength, inChanNum;
        float[] fftBuf, convBuf1;
        float[][] inBuf;
        int inBufOff;
        int framesRead;
        int chunkLength;
        boolean chirp = pr.intg[PR_TYPE] == TYPE_CZT;
        boolean fourier = pr.intg[PR_TYPE] == TYPE_FFT;
        double loFreq, hiFreq, loRadius, hiRadius, chirpAngle, chirpRadius;
        double[] chirpImpulse = null;
        float[] fftBuf2 = null;
        int oversmp = pr.intg[PR_OVERSMP];
        if ((pr.text[PR_FILENAME] == null) || (pr.text[PR_FILENAME].length() == 0)) {
            FileDialog fDlg;
            String fiName, fiDir;
            final Component ownerF = owner.getWindow().getWindow();
            final boolean makeF = !(ownerF instanceof Frame);
            final Frame f = makeF ? new Frame() : (Frame) ownerF;
            fDlg = new FileDialog(f, ((OpIcon) getIcon()).getName() + ": Select inputfile");
            fDlg.setVisible(true);
            if (makeF) f.dispose();
            fiName = fDlg.getFile();
            fiDir = fDlg.getDirectory();
            fDlg.dispose();
            if (fiDir == null) fiDir = "";
            if (fiName == null) {
                runQuit(new IOException(ERR_NOINPUT));
                return;
            }
            runFileName = fiDir + fiName;
        } else {
            runFileName = pr.text[PR_FILENAME];
        }
        try {
            runFile = AudioFile.openAsRead(new File(runFileName));
            runInStream = runFile.getDescr();
            inLength = (int) runInStream.length;
            inChanNum = runInStream.channels;
            inLengthP = new Param(AudioFileDescr.samplesToMillis(runInStream, inLength), Param.ABS_MS);
            startShift = Param.transform(pr.para[PR_STARTSHIFT], Param.ABS_MS, inLengthP, null).val;
            outLength = Param.transform(pr.para[PR_LENGTH], Param.ABS_MS, inLengthP, null).val;
            tmpStream = new SpectStream();
            tmpStream.smpRate = (float) runInStream.rate;
            loFreq = Param.transform(pr.para[PR_LOFREQ], Param.ABS_HZ, null, tmpStream).val * Constants.PI2 / tmpStream.smpRate;
            hiFreq = Param.transform(pr.para[PR_HIFREQ], Param.ABS_HZ, null, tmpStream).val * Constants.PI2 / tmpStream.smpRate;
            loRadius = Param.transform(pr.para[PR_LORADIUS], Param.ABS_AMP, new Param(1.0, Param.ABS_AMP), null).val;
            hiRadius = Param.transform(pr.para[PR_HIRADIUS], Param.ABS_AMP, new Param(1.0, Param.ABS_AMP), null).val;
            if (pr.bool[PR_ADJUSTSTART]) {
                runStartFrame = Math.min(inLength, (int) (AudioFileDescr.millisToSamples(runInStream, startShift) + 0.5));
            } else {
                runStartFrame = 0;
            }
            runFile.seekFrame(runStartFrame);
            if (pr.bool[PR_ADJUSTLENGTH]) {
                runInLength = Math.min(inLength - runStartFrame, (int) (AudioFileDescr.millisToSamples(runInStream, outLength) + 0.5));
            } else {
                runInLength = inLength - runStartFrame;
            }
            runFrames = (runInLength + winSize - 1) / winStep;
            if (chirp) {
                fftLength = winSize << (oversmp + 1);
                fullFFTlength = fftLength << 1;
                fftBuf2 = new float[fullFFTlength + 2];
                Util.clear(fftBuf2);
                chirpImpulse = new double[fullFFTlength + 2];
            } else {
                if (!fourier) oversmp++;
                fftLength = winSize << oversmp;
                fullFFTlength = fftLength;
            }
            bands = (winSize << oversmp >> 1);
            fftBuf = new float[fullFFTlength + 2];
            inBuf = new float[inChanNum][winSize];
            Util.clear(inBuf);
            win = Filter.createFullWindow(winSize, pr.intg[PR_WINDOW]);
            if (chirp) {
                d5 = (double) bands;
                chirpRadius = Math.pow(hiRadius / loRadius, 1.0 / d5);
                chirpAngle = (hiFreq - loFreq) / d5;
                d3 = 1.0 / Math.sqrt(chirpRadius);
                d4 = chirpAngle * -0.5;
                for (i = 0, j = 0, m = fullFFTlength; i <= fftLength; j++) {
                    k = j * j;
                    d1 = Math.pow(d3, k);
                    d2 = d4 * k;
                    chirpImpulse[i] = 1.0 / d1;
                    fftBuf2[i] = (float) (d1 * Math.cos(d2));
                    chirpImpulse[m] = chirpImpulse[i];
                    fftBuf2[m] = fftBuf2[i];
                    i++;
                    m++;
                    chirpImpulse[i] = -d2;
                    fftBuf2[i] = (float) (d1 * Math.sin(d2));
                    chirpImpulse[m] = chirpImpulse[i];
                    fftBuf2[m] = fftBuf2[i];
                    i++;
                    m -= 3;
                }
                Fourier.complexTransform(fftBuf2, fftLength, Fourier.FORWARD);
                d3 = 1.0 / loRadius;
                d4 = -loFreq;
                for (i = 0, d6 = 0.0; i < winSize; i++) {
                    d6 += (double) win[i];
                }
                d6 = 1.0 / d6;
                convBuf1 = win;
                win = new float[win.length << 1];
                k = 0;
                for (i = 0, j = 0; j < winSize; j++) {
                    d1 = (double) convBuf1[j] * d6 * Math.pow(d3, j) * chirpImpulse[k++];
                    d2 = d4 * j + chirpImpulse[k++];
                    win[i++] = (float) (d1 * Math.cos(d2));
                    win[i++] = (float) (d1 * Math.sin(d2));
                }
            } else {
                for (i = 0, d1 = 0.0; i < winSize; i++) {
                    d1 += (double) win[i];
                }
                f1 = (float) (1.0 / d1);
                for (i = 0; i < winSize; i++) {
                    win[i] *= f1;
                }
            }
            i = (bands << 3) * inChanNum;
            runOutStream = new SpectStream(Math.max(2, 0x40000 / i));
            runOutStream.setChannels(inChanNum);
            runOutStream.setBands(0.0f, (float) runInStream.rate / 2, bands + 1, SpectStream.MODE_LIN);
            runOutStream.setRate((float) runInStream.rate, winStep);
            runOutStream.setEstimatedLength(runFrames);
            runOutSlot = ((SpectStreamSlot) slots.elementAt(SLOT_OUTPUT));
            runOutSlot.initWriter(runOutStream);
            runSlotsReady();
            inBufOff = 0;
            framesRead = 0;
            chunkLength = winStep;
            while ((runFramesRead < runFrames) && !threadDead) {
                len = Math.min(chunkLength, runInLength - framesRead);
                off = (inBufOff + winSize - chunkLength) % winSize;
                runFile.readFrames(inBuf, off, len);
                if (len < chunkLength) {
                    for (ch = 0; ch < inChanNum; ch++) {
                        convBuf1 = inBuf[ch];
                        for (j = off + len, k = off + chunkLength; j < k; ) {
                            convBuf1[j++] = 0.0f;
                        }
                    }
                }
                runFramesRead++;
                framesRead += len;
                runOutFr = runOutStream.allocFrame();
                for (ch = 0; ch < inChanNum; ch++) {
                    convBuf1 = inBuf[ch];
                    switch(pr.intg[PR_TYPE]) {
                        case TYPE_CZT:
                            if (pr.bool[PR_ROTATE]) {
                                for (i = inBufOff, j = fullFFTlength - winSize, k = 0; k < winSize; ) {
                                    m = Math.min(winSize - k, Math.min(winSize - i, (fullFFTlength - j) >> 1));
                                    k += m;
                                    for (; m > 0; m--) {
                                        f1 = convBuf1[i++];
                                        fftBuf[j++] = f1;
                                        fftBuf[j++] = f1;
                                    }
                                    i %= winSize;
                                    j %= fullFFTlength;
                                }
                                for (i = 0, j = winSize; i < winSize; i++, j++) {
                                    fftBuf[i] *= win[j];
                                }
                                for (; i < fullFFTlength - winSize; i++) {
                                    fftBuf[i] = 0.0f;
                                }
                                for (j = 0; i < fullFFTlength; i++, j++) {
                                    fftBuf[i] *= win[j];
                                }
                                Fourier.complexTransform(fftBuf, fftLength, Fourier.FORWARD);
                                Fourier.complexMult(fftBuf2, 0, fftBuf, 0, fftBuf, 0, fullFFTlength);
                                Fourier.complexTransform(fftBuf, fftLength, Fourier.INVERSE);
                                convBuf1 = runOutFr.data[ch];
                                for (i = 0, j = fullFFTlength - winSize, k = 0; i <= bands; ) {
                                    m = Math.min((fullFFTlength - j) >> 1, bands + 1 - i);
                                    i += m;
                                    for (; m > 0; m--) {
                                        d1 = fftBuf[j++];
                                        d2 = fftBuf[j++];
                                        d3 = chirpImpulse[k];
                                        d4 = chirpImpulse[k + 1];
                                        convBuf1[k++] = (float) (Math.sqrt(d1 * d1 + d2 * d2) * d3);
                                        convBuf1[k++] = (float) (Math.atan2(d2, d1) + d4);
                                    }
                                    j %= fullFFTlength;
                                }
                            } else {
                                for (i = inBufOff, j = 0, k = 0; k < winSize; ) {
                                    m = Math.min(winSize - k, winSize - i);
                                    k += m;
                                    for (; m > 0; m--) {
                                        f1 = convBuf1[i++];
                                        fftBuf[j] = f1 * win[j];
                                        j++;
                                        fftBuf[j] = f1 * win[j];
                                        j++;
                                    }
                                    i %= winSize;
                                }
                                for (; j < fullFFTlength; j++) {
                                    fftBuf[j] = 0.0f;
                                }
                                Fourier.complexTransform(fftBuf, fftLength, Fourier.FORWARD);
                                Fourier.complexMult(fftBuf2, 0, fftBuf, 0, fftBuf, 0, fullFFTlength);
                                Fourier.complexTransform(fftBuf, fftLength, Fourier.INVERSE);
                                convBuf1 = runOutFr.data[ch];
                                for (i = 0, j = 0; i <= bands; i++, j += 2) {
                                    k = j + 1;
                                    d1 = fftBuf[j];
                                    d2 = fftBuf[k];
                                    d3 = chirpImpulse[j];
                                    d4 = chirpImpulse[k];
                                    convBuf1[j] = (float) (Math.sqrt(d1 * d1 + d2 * d2) * d3);
                                    convBuf1[k] = (float) (Math.atan2(d2, d1) + d4);
                                }
                            }
                            break;
                        case TYPE_FFT:
                        case TYPE_NONE:
                            if (pr.bool[PR_ROTATE]) {
                                for (i = inBufOff, j = fftLength - winHalf, k = 0; k < winSize; ) {
                                    m = Math.min(winSize - k, Math.min(winSize - i, fftLength - j));
                                    System.arraycopy(convBuf1, i, fftBuf, j, m);
                                    i = (i + m) % winSize;
                                    j = (j + m) % fftLength;
                                    k += m;
                                }
                                for (i = 0, j = winHalf; i < winHalf; i++, j++) {
                                    fftBuf[i] *= win[j];
                                }
                                for (; i < fftLength - winHalf; i++) {
                                    fftBuf[i] = 0.0f;
                                }
                                for (j = 0; i < fftLength; i++, j++) {
                                    fftBuf[i] *= win[j];
                                }
                            } else {
                                for (i = inBufOff, j = 0, k = 0; k < winSize; ) {
                                    m = Math.min(winSize - k, winSize - i);
                                    for (; m > 0; m--) {
                                        fftBuf[j++] = convBuf1[i++] * win[k++];
                                    }
                                    i %= winSize;
                                }
                                for (; j < fftLength; j++) {
                                    fftBuf[j] = 0.0f;
                                }
                            }
                            convBuf1 = runOutFr.data[ch];
                            if (fourier) {
                                Fourier.realTransform(fftBuf, fftLength, Fourier.FORWARD);
                            } else {
                                if (pr.bool[PR_ROTATE]) {
                                    for (i = fftLength - winSize, j = fftLength - winHalf; i < winSize; ) {
                                        fftBuf[i++] = fftBuf[j++];
                                        fftBuf[i++] = 0.0f;
                                    }
                                    for (i = winHalf, j = winSize; i > 0; ) {
                                        fftBuf[--j] = 0.0f;
                                        fftBuf[--j] = fftBuf[--i];
                                    }
                                } else {
                                    for (i = winSize, j = winSize * 2; i > 0; ) {
                                        fftBuf[--j] = 0.0f;
                                        fftBuf[--j] = fftBuf[--i];
                                    }
                                }
                            }
                            Fourier.rect2Polar(fftBuf, 0, convBuf1, 0, fftLength + 2);
                            break;
                    }
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
                inBufOff = (inBufOff + chunkLength) % winSize;
            }
            runFile.close();
            runOutStream.closeWriter();
        } catch (IOException e) {
            if (runFile != null) {
                runFile.cleanUp();
            }
            runQuit(e);
            return;
        } catch (SlotAlreadyConnectedException e) {
            if (runFile != null) {
                runFile.cleanUp();
            }
            runQuit(e);
            return;
        }
        runQuit(null);
    }
