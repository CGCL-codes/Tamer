    public void process() {
        int i, j, k;
        int off, chunkLength;
        int ch;
        long progOff, progLen;
        float f1;
        AudioFile inF = null;
        AudioFile outF = null;
        AudioFile impF = null;
        FloatFile[] floatF = null;
        File[] tempFile = null;
        AudioFileDescr inStream = null;
        AudioFileDescr outStream = null;
        AudioFileDescr impStream = null;
        int inChanNum;
        int impChanNum = 0;
        float[][] inBuf;
        float[][] impBuf = null;
        float[] zData;
        float[] fftBuf;
        float[] win;
        float[] convBuf1, convBuf2;
        PathField ggOutput;
        float mean;
        float stddev;
        float critical;
        float probBound;
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        float minAmp;
        int checkOff, checkInner, checkLen;
        int chunkMin, chunkMax, chunkNum, chunkDone;
        int fadeLen, prePost, frameSize, inOff;
        int firstClk, lastClk;
        int markerID = -1;
        Marker marker = null;
        java.util.List markers, outMarkers;
        Graphics g = null;
        Dimension gDim;
        int x;
        int inLength;
        int outLength = 0;
        int impLength = 0;
        int framesRead, framesWritten;
        topLevel: try {
            minAmp = (float) (Param.transform(pr.para[PR_MINAMP], Param.ABS_AMP, ampRef, null)).val;
            switch(pr.intg[PR_PROBBOUND]) {
                case PROB_5PM:
                    probBound = 2.58f;
                    break;
                case PROB_1PM:
                    probBound = 3.10f;
                    break;
                default:
                case PROB_05PM:
                    probBound = 3.30f;
                    break;
            }
            g = lbClicks.getGraphics();
            gDim = lbClicks.getSize();
            if (g != null) {
                lbClicks.repaint();
                g.setColor(Color.red);
            }
            switch(pr.intg[PR_CHECKSIZE]) {
                case CHECK_16:
                    checkLen = 16;
                    break;
                case CHECK_32:
                    checkLen = 32;
                    break;
                default:
                case CHECK_64:
                    checkLen = 64;
                    break;
            }
            switch(pr.intg[PR_XFADE]) {
                case XFADE_512:
                    fadeLen = 512;
                    break;
                case XFADE_1024:
                    fadeLen = 1024;
                    break;
                default:
                case XFADE_2048:
                    fadeLen = 2048;
                    break;
            }
            prePost = fadeLen >> 1;
            frameSize = prePost + checkLen / 4 + checkLen / 2 + checkLen / 4 + prePost;
            checkOff = prePost + (checkLen >> 2);
            checkInner = checkLen >> 1;
            inOff = frameSize - checkInner;
            inF = AudioFile.openAsRead(new File(pr.text[PR_INPUTFILE]));
            inStream = inF.getDescr();
            inChanNum = inStream.channels;
            inLength = (int) inStream.length;
            if ((inLength * inChanNum) < 1) throw new EOFException(ERR_EMPTY);
            markers = (java.util.List) inStream.getProperty(AudioFileDescr.KEY_MARKERS);
            if (markers != null) {
                outMarkers = new Vector(markers);
            } else {
                outMarkers = new Vector();
            }
            if (pr.bool[PR_FSCAPEREPAIR] || pr.bool[PR_MARKERREPAIR]) {
                impF = AudioFile.openAsRead(new File(pr.text[PR_IMPULSEFILE]));
                impStream = impF.getDescr();
                impChanNum = impStream.channels;
                impLength = (int) Math.min(impStream.length, prePost);
                impBuf = new float[impChanNum][fadeLen + 2];
                for (ch = 0; ch < impChanNum; ch++) {
                    convBuf1 = impBuf[ch];
                    for (i = 0; i < convBuf1.length; i++) {
                        convBuf1[i] = 0.0f;
                    }
                }
                ggOutput = (PathField) gui.getItemObj(GG_OUTPUTFILE);
                if (ggOutput == null) throw new IOException(ERR_MISSINGPROP);
                outStream = new AudioFileDescr(inStream);
                ggOutput.fillStream(outStream);
                IOUtil.createEmptyFile(new File(pr.text[PR_OUTPUTFILE]));
                outLength = inLength;
            }
            if (!threadRunning) break topLevel;
            inBuf = new float[inChanNum][frameSize];
            zData = new float[checkLen];
            fftBuf = new float[fadeLen + 2];
            chunkMin = frameSize / checkInner;
            chunkNum = ((inLength + checkInner - 1) / checkInner) + (chunkMin - 1);
            chunkMax = chunkNum - chunkMin;
            chunkDone = 0;
            win = Filter.createWindow(prePost, Filter.WIN_BLACKMAN);
            for (ch = 0; ch < inChanNum; ch++) {
                convBuf1 = inBuf[ch];
                for (i = 0; i < frameSize; i++) {
                    convBuf1[i] = 0.0f;
                }
            }
            for (i = 0; i < fftBuf.length; i++) {
                fftBuf[i] = 0.0f;
            }
            if (pr.bool[PR_MARKERDETECT] && markers != null) {
                Collections.sort(markers);
                markerID = Marker.find(markers, MARK_CLICK, 0);
                if (markerID >= 0) {
                    marker = (Marker) markers.get(markerID);
                }
            }
            progOff = 0;
            progLen = (long) inLength + (long) outLength + (long) (chunkNum * checkInner);
            if (pr.bool[PR_FSCAPEREPAIR] && !pr.bool[PR_MARKERREPAIR]) {
                outF = AudioFile.openAsWrite(outStream);
            }
            if (pr.bool[PR_MARKERREPAIR]) {
                tempFile = new File[inChanNum];
                floatF = new FloatFile[inChanNum];
                for (ch = 0; ch < inChanNum; ch++) {
                    tempFile[ch] = null;
                    floatF[ch] = null;
                }
                for (ch = 0; ch < inChanNum; ch++) {
                    tempFile[ch] = IOUtil.createTempFile();
                    floatF[ch] = new FloatFile(tempFile[ch], GenericFile.MODE_OUTPUT);
                }
                progLen += (long) outLength;
            }
            if (!threadRunning) break topLevel;
            if (impF != null) {
                progLen += (long) impLength;
                impF.readFrames(impBuf, 0, impLength);
                for (ch = 0; ch < impChanNum; ch++) {
                    Fourier.realTransform(impBuf[ch], fadeLen, Fourier.FORWARD);
                }
                impF.close();
                impF = null;
                progOff += impLength;
                setProgression((float) progOff / (float) progLen);
            }
            if (!threadRunning) break topLevel;
            framesRead = 0;
            framesWritten = 0;
            while (threadRunning && (chunkDone < chunkNum)) {
                for (off = 0, chunkLength = Math.min(inLength - framesRead, checkInner); threadRunning && (off < chunkLength); ) {
                    j = Math.min(8192, chunkLength - off);
                    inF.readFrames(inBuf, off + inOff, j);
                    framesRead += j;
                    off += j;
                    progOff += j;
                    setProgression((float) progOff / (float) progLen);
                }
                if (!threadRunning) break topLevel;
                for (ch = 0; ch < inChanNum; ch++) {
                    convBuf1 = inBuf[ch];
                    for (i = chunkLength + inOff; i < frameSize; i++) {
                        convBuf1[i] = 0.0f;
                    }
                }
                firstClk = checkInner;
                lastClk = -1;
                if (pr.bool[PR_FSCAPEDETECT]) {
                    for (ch = 0; ch < inChanNum; ch++) {
                        convBuf1 = inBuf[ch];
                        mean = 0f;
                        for (i = prePost, j = 0; j < checkLen; i++, j++) {
                            zData[j] = convBuf1[i + 1] - convBuf1[i];
                            mean += zData[j];
                        }
                        mean /= checkLen;
                        stddev = 0f;
                        for (j = 0; j < checkLen; j++) {
                            f1 = zData[j] - mean;
                            stddev += f1 * f1;
                        }
                        stddev = (float) Math.sqrt(stddev / checkLen);
                        critical = probBound * stddev;
                        for (i = 0, j = (checkLen >> 2) - 1; i < checkInner; i++, j++) {
                            if ((Math.abs(zData[j] - mean) >= critical) && (Math.abs(zData[j]) >= minAmp)) {
                                firstClk = Math.min(firstClk, i);
                                lastClk = Math.max(lastClk, i);
                                if (pr.bool[PR_MARKERREPAIR]) {
                                    outMarkers.add(new Marker((chunkDone - chunkMin + 1) * checkInner + i + checkOff, MARK_CLICK));
                                }
                            }
                        }
                    }
                }
                if (pr.bool[PR_MARKERDETECT]) {
                    i = (chunkDone - chunkMin + 1) * checkInner + checkOff;
                    while (marker != null) {
                        if ((marker.pos >= i) && (marker.pos < (i + checkInner))) {
                            firstClk = Math.min(firstClk, (int) marker.pos - i);
                            lastClk = Math.max(lastClk, (int) marker.pos - i);
                            markerID = Marker.find(markers, MARK_CLICK, markerID + 1);
                            if (markerID >= 0) {
                                marker = (Marker) markers.get(markerID);
                            } else {
                                marker = null;
                            }
                        } else if (marker.pos < i) {
                            markerID = Marker.find(markers, MARK_CLICK, markerID + 1);
                            if (markerID >= 0) {
                                marker = (Marker) markers.get(markerID);
                            } else {
                                marker = null;
                            }
                        } else break;
                    }
                }
                declick: if ((lastClk >= 0) && (chunkDone >= chunkMin) && (chunkDone <= chunkMax)) {
                    if (g != null) {
                        x = (int) (((float) chunkDone / (float) chunkNum) * gDim.width + 0.5f);
                        g.drawLine(x, 0, x, gDim.height - 1);
                    }
                    if (!pr.bool[PR_FSCAPEREPAIR]) break declick;
                    for (ch = 0; ch < inChanNum; ch++) {
                        convBuf1 = inBuf[ch];
                        System.arraycopy(convBuf1, firstClk + checkOff - prePost, fftBuf, 0, prePost);
                        for (i = prePost; i < fadeLen; i++) {
                            fftBuf[i] = 0.0f;
                        }
                        for (i = 0, j = prePost - 1; j >= 0; i++, j -= 2) {
                            fftBuf[i] *= win[j];
                        }
                        for (j = 0; j < prePost; i++, j += 2) {
                            fftBuf[i] *= win[j];
                        }
                        Fourier.realTransform(fftBuf, fadeLen, Fourier.FORWARD);
                        convBuf2 = impBuf[ch % impChanNum];
                        for (i = 0; i <= fadeLen; i += 2) {
                            j = i + 1;
                            f1 = fftBuf[i];
                            fftBuf[i] = f1 * convBuf2[i] - fftBuf[j] * convBuf2[j];
                            fftBuf[j] = f1 * convBuf2[j] + fftBuf[j] * convBuf2[i];
                        }
                        Fourier.realTransform(fftBuf, fadeLen, Fourier.INVERSE);
                        for (j = (prePost >> 1), i = firstClk + checkOff - j, k = 0; j < prePost; i++, j++, k += 2) {
                            f1 = win[k];
                            convBuf1[i] = f1 * convBuf1[i] + (1.0f - f1) * fftBuf[j];
                        }
                        for (k = lastClk + checkOff; i <= k; i++, j++) {
                            convBuf1[i] = fftBuf[j];
                        }
                        for (k = 0; k < prePost; k += 2, i++, j++) {
                            f1 = win[k];
                            convBuf1[i] = (1.0f - f1) * convBuf1[i] + f1 * fftBuf[j];
                        }
                    }
                }
                chunkDone++;
                progOff += checkInner;
                setProgression((float) progOff / (float) progLen);
                if (!threadRunning) break topLevel;
                if (chunkDone >= chunkMin) {
                    chunkLength = Math.min(outLength - framesWritten, checkInner);
                    if (floatF != null) {
                        for (ch = 0; ch < inChanNum; ch++) {
                            floatF[ch].writeFloats(inBuf[ch], 0, chunkLength);
                        }
                        framesWritten += chunkLength;
                        off += chunkLength;
                        progOff += chunkLength;
                        setProgression((float) progOff / (float) progLen);
                    } else if (outF != null) {
                        for (off = 0; threadRunning && (off < chunkLength); ) {
                            j = Math.min(8192, chunkLength - off);
                            outF.writeFrames(inBuf, off, j);
                            framesWritten += j;
                            off += j;
                            progOff += j;
                            setProgression((float) progOff / (float) progLen);
                        }
                    }
                }
                for (ch = 0; ch < inChanNum; ch++) {
                    convBuf1 = inBuf[ch];
                    System.arraycopy(convBuf1, checkInner, convBuf1, 0, frameSize - checkInner);
                }
            }
            if (!threadRunning) break topLevel;
            if (floatF != null) {
                Collections.sort(outMarkers);
                if (outMarkers.size() > 0xFFFF) {
                    System.out.println("WARNING: too many markers (" + outMarkers.size() + "). Truncating to 65535!");
                    while (outMarkers.size() > 0xFFFF) outMarkers.remove(0xFFFF);
                }
                outStream.setProperty(AudioFileDescr.KEY_MARKERS, outMarkers);
                outF = AudioFile.openAsWrite(outStream);
                normalizeAudioFile(floatF, outF, inBuf, 1.0f, 1.0f);
                for (ch = 0; ch < inChanNum; ch++) {
                    floatF[ch].cleanUp();
                    floatF[ch] = null;
                    tempFile[ch].delete();
                    tempFile[ch] = null;
                }
            }
            if (outF != null) {
                outF.close();
                outF = null;
            }
            inF.close();
            inF = null;
        } catch (IOException e1) {
            setError(e1);
        } catch (OutOfMemoryError e2) {
            inStream = null;
            outStream = null;
            impStream = null;
            inBuf = null;
            zData = null;
            fftBuf = null;
            win = null;
            System.gc();
            setError(new Exception(ERR_MEMORY));
            ;
        }
        if (g != null) {
            g.dispose();
        }
        if (inF != null) {
            inF.cleanUp();
        }
        if (outF != null) {
            outF.cleanUp();
        }
        if (impF != null) {
            impF.cleanUp();
            impF = null;
        }
        if (floatF != null) {
            for (ch = 0; ch < floatF.length; ch++) {
                if (floatF[ch] != null) floatF[ch].cleanUp();
                if (tempFile[ch] != null) tempFile[ch].delete();
            }
        }
    }
