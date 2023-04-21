    protected void process() {
        int i, j, k, len;
        long progOff, progLen;
        float f1, f2;
        RandomAccessFile inF = null;
        AudioFile outF = null;
        AudioFileDescr outStream = null;
        ByteBuffer bb;
        FileChannel inCh;
        int bufSize;
        Info info;
        float[][] outBuf;
        float gain;
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        long inLength;
        long framesRead;
        long pos, lastPos;
        float maxAmp, lastMaxAmp, diff;
        int chunkCount, type, lastType, offset, lastOffset;
        int[] stat = new int[5];
        float[] amp = new float[4];
        float[] prob = new float[4];
        PathField ggOutput;
        topLevel: try {
            inF = new RandomAccessFile(pr.text[PR_INPUTFILE], "r");
            inLength = inF.length();
            inCh = inF.getChannel();
            progOff = 0;
            progLen = inLength * 2;
            bufSize = (int) pr.para[PR_LENGTH].val / 3 * 3;
            ggOutput = (PathField) gui.getItemObj(GG_OUTPUTFILE);
            if (ggOutput == null) throw new IOException(ERR_MISSINGPROP);
            outStream = new AudioFileDescr();
            ggOutput.fillStream(outStream);
            outStream.channels = 1;
            outF = AudioFile.openAsWrite(outStream);
            if (!threadRunning) break topLevel;
            gain = (float) ((Param.transform(pr.para[PR_GAIN], Param.ABS_AMP, ampRef, null)).val);
            if (!threadRunning) break topLevel;
            framesRead = 0L;
            lastType = 0;
            lastMaxAmp = 0.0f;
            lastPos = 0L;
            chunkCount = 0;
            lastOffset = 0;
            info = new Info();
            bb = ByteBuffer.allocate(bufSize);
            info.bb = bb;
            outBuf = new float[1][bb.capacity()];
            info.fb = outBuf[0];
            while (threadRunning && framesRead < inLength) {
                bb.clear();
                len = (int) Math.min(inLength - framesRead, bufSize);
                inCh.read(bb);
                type = 0;
                f2 = idByte(info, 0);
                offset = 0;
                maxAmp = info.maxAmp;
                diff = Float.POSITIVE_INFINITY;
                for (i = 0, j = 0; i < 2; i++) {
                    prob[i] = idShort(info, i);
                    amp[i] = info.maxAmp;
                    if (info.diff < diff) {
                        j = i;
                        diff = info.diff;
                    }
                }
                if (prob[j] < f2) {
                    f2 = prob[j];
                    type = 1;
                    maxAmp = amp[j];
                    offset = j;
                }
                diff = Float.POSITIVE_INFINITY;
                for (i = 0, j = 0; i < 3; i++) {
                    prob[i] = idTri(info, i);
                    amp[i] = info.maxAmp;
                    if (info.diff < diff) {
                        j = i;
                        diff = info.diff;
                    }
                }
                if (prob[j] < f2) {
                    f2 = prob[j];
                    type = 2;
                    maxAmp = amp[j];
                    offset = j;
                }
                diff = Float.POSITIVE_INFINITY;
                for (i = 0, j = 0; i < 4; i++) {
                    prob[i] = idInt(info, i);
                    amp[i] = info.maxAmp;
                    if (info.diff < diff) {
                        j = i;
                        diff = info.diff;
                    }
                }
                if (prob[j] < f2) {
                    f2 = prob[j];
                    type = 3;
                    maxAmp = amp[j];
                    offset = j;
                }
                diff = Float.POSITIVE_INFINITY;
                for (i = 0, j = 0; i < 4; i++) {
                    prob[i] = idFloat(info, i);
                    amp[i] = info.maxAmp;
                    if (info.diff < diff) {
                        j = i;
                        diff = info.diff;
                    }
                }
                if (prob[j] < f2) {
                    f2 = prob[j];
                    type = 3;
                    maxAmp = amp[j];
                    offset = j;
                }
                if ((type != lastType) || (offset != 0) || (len < bufSize)) {
                    pos = inCh.position();
                    inCh.position(lastPos);
                    f1 = lastMaxAmp > 0.0f ? (gain / lastMaxAmp) : 0.0f;
                    stat[lastType]++;
                    for (i = 0; i < chunkCount; i++) {
                        bb.clear();
                        inCh.read(bb);
                        j = info.fb.length;
                        switch(lastType) {
                            case 0:
                                copyBytes(info, lastOffset);
                                break;
                            case 1:
                                copyShorts(info, lastOffset);
                                j = (j - lastOffset) / 2;
                                break;
                            case 2:
                                copyTris(info, lastOffset);
                                j = (j - lastOffset) / 3;
                                break;
                            case 3:
                                copyInts(info, lastOffset);
                                j = (j - lastOffset) / 4;
                                break;
                            case 4:
                                copyFloats(info, lastOffset);
                                j = (j - lastOffset) / 4;
                                break;
                        }
                        for (k = 0; k < j; k++) {
                            info.fb[k] *= f1;
                        }
                        outF.writeFrames(outBuf, 0, j);
                        progOff += bufSize;
                        setProgression((float) progOff / (float) progLen);
                        if (!threadRunning) break topLevel;
                    }
                    lastType = type;
                    lastPos = pos;
                    lastOffset = offset;
                    inCh.position(pos + offset);
                    lastMaxAmp = maxAmp;
                    chunkCount = 1;
                } else {
                    lastMaxAmp = Math.max(maxAmp, lastMaxAmp);
                    chunkCount++;
                }
                framesRead += len;
                progOff += len;
                setProgression((float) progOff / (float) progLen);
            }
            if (!threadRunning) break topLevel;
            inF.close();
            inF = null;
            outF.close();
            outF = null;
            setProgression(1.0f);
            System.out.println("# of chunks: " + stat[0] + " bytes, " + stat[1] + " shorts, " + stat[2] + " tris, " + stat[3] + " ints, " + stat[4] + " floats.");
        } catch (IOException e1) {
            setError(e1);
        } catch (OutOfMemoryError e2) {
            outStream = null;
            System.gc();
            setError(new Exception(ERR_MEMORY));
            ;
        }
        if (inF != null) {
            try {
                inF.close();
            } catch (Exception e11) {
            }
        }
        if (outF != null) {
            outF.cleanUp();
        }
    }
