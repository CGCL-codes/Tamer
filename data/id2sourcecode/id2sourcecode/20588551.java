    private void clearRangeOvr(Span clearSpan, int mode, Object source, AbstractCompoundEdit ce, boolean[] trackMap, BlendContext bc) throws IOException {
        final long blendLen = bc == null ? 0L : bc.getLen();
        boolean sync = true;
        boolean success = false;
        final boolean t1 = trackMap[0];
        for (int i = 1; i < trackMap.length; i++) {
            if (t1 != trackMap[i]) {
                sync = false;
                break;
            }
        }
        final List collStakes = new ArrayList(3);
        try {
            if (sync && !t1) return;
            final double perDecProgRatio = 0.9;
            final double progRatio = 1.0 / (1.0 + ((1.0 - perDecProgRatio) / perDecProgRatio) * numDepDec);
            final boolean hasBlend = blendLen > 0L;
            final long blendLen2 = blendLen << 1;
            final int numCh = this.getChannelNum();
            final float[][] readWriteBufF = new float[numCh][];
            final float[][] fadeBuf;
            final float[][] readBufS;
            final float[][] writeBufS;
            final Span silentSpan = new Span(clearSpan.start + blendLen, clearSpan.stop - blendLen);
            final long silentLen = silentSpan.getLength();
            final int bufLen = (int) Math.min(clearSpan.getLength(), BUFSIZE);
            final boolean useSilentStake = sync && (silentLen >= MINSILENTSIZE);
            final AudioStake writeStake1, writeStake2, writeStake3;
            final double progWeight;
            float[] empty = null;
            float[] temp;
            int chunkLen;
            long n;
            long totalFramesWritten = 0;
            Span chunkSpan;
            flushProgression();
            if (hasBlend) {
                fadeBuf = new float[numCh][];
                for (int i = 0; i < trackMap.length; i++) {
                    readWriteBufF[i] = new float[bufLen];
                    if (trackMap[i]) fadeBuf[i] = readWriteBufF[i];
                }
            } else {
                fadeBuf = null;
            }
            if (!useSilentStake) {
                readBufS = new float[numCh][];
                writeBufS = new float[numCh][];
                for (int i = 0; i < trackMap.length; i++) {
                    if (trackMap[i]) {
                        if (empty == null) empty = new float[bufLen];
                        writeBufS[i] = empty;
                    }
                    if (!trackMap[i]) {
                        if (readWriteBufF[i] != null) {
                            readBufS[i] = readWriteBufF[i];
                        } else {
                            readBufS[i] = new float[bufLen];
                        }
                        writeBufS[i] = readBufS[i];
                    }
                }
            } else {
                readBufS = null;
                writeBufS = null;
            }
            if (useSilentStake) {
                if (hasBlend) {
                    writeStake1 = alloc(new Span(clearSpan.start, silentSpan.start));
                    collStakes.add(writeStake1);
                } else {
                    writeStake1 = null;
                }
                writeStake2 = allocSilent(silentSpan);
                collStakes.add(writeStake2);
                if (hasBlend) {
                    writeStake3 = alloc(new Span(silentSpan.stop, clearSpan.stop));
                    collStakes.add(writeStake3);
                } else {
                    writeStake3 = null;
                }
                final double progRatio2 = 1.0 / (1.0 + numDepDec);
                final double w = (double) blendLen2 / (blendLen2 + silentLen);
                progWeight = (progRatio * w + progRatio2 * (1.0 - w)) / blendLen2;
            } else {
                writeStake2 = alloc(clearSpan);
                writeStake1 = writeStake2;
                writeStake3 = writeStake2;
                collStakes.add(writeStake2);
                progWeight = progRatio / (blendLen2 + silentLen);
            }
            for (long framesWritten = 0; framesWritten < blendLen; ) {
                chunkLen = (int) Math.min(bufLen, blendLen - framesWritten);
                n = clearSpan.start + framesWritten;
                chunkSpan = new Span(n, n + chunkLen);
                this.readFrames(readWriteBufF, 0, chunkSpan);
                bc.fadeOut(framesWritten, fadeBuf, 0, fadeBuf, 0, chunkLen);
                writeStake1.writeFrames(readWriteBufF, 0, chunkSpan);
                framesWritten += chunkLen;
                totalFramesWritten += chunkLen;
                setProgression(totalFramesWritten, progWeight);
            }
            if (!useSilentStake) {
                if (hasBlend) {
                    for (int i = 0; i < numCh; i++) {
                        temp = writeBufS[i];
                        if (temp != empty) {
                            for (int j = 0; j < bufLen; j++) {
                                temp[j] = 0f;
                            }
                        }
                    }
                }
                for (long framesWritten = 0; framesWritten < silentLen; ) {
                    chunkLen = (int) Math.min(bufLen, silentLen - framesWritten);
                    n = silentSpan.start + framesWritten;
                    chunkSpan = new Span(n, n + chunkLen);
                    if (!sync) {
                        this.readFrames(readBufS, 0, chunkSpan);
                    }
                    writeStake2.writeFrames(writeBufS, 0, chunkSpan);
                    framesWritten += chunkLen;
                    totalFramesWritten += chunkLen;
                    setProgression(totalFramesWritten, progWeight);
                }
            }
            for (long framesWritten = 0; framesWritten < blendLen; ) {
                chunkLen = (int) Math.min(bufLen, blendLen - framesWritten);
                n = silentSpan.stop + framesWritten;
                chunkSpan = new Span(n, n + chunkLen);
                this.readFrames(readWriteBufF, 0, chunkSpan);
                bc.fadeIn(framesWritten, fadeBuf, 0, fadeBuf, 0, chunkLen);
                writeStake3.writeFrames(readWriteBufF, 0, chunkSpan);
                framesWritten += chunkLen;
                totalFramesWritten += chunkLen;
                setProgression(totalFramesWritten, progWeight);
            }
            if (useSilentStake) {
                if (writeStake1 != null) writeStake1.flush();
                if (writeStake3 != null) writeStake3.flush();
            } else {
                writeStake2.flush();
            }
            success = true;
        } finally {
            if (!success) {
                for (int i = 0; i < collStakes.size(); i++) {
                    ((AudioStake) collStakes.get(i)).dispose();
                }
            }
        }
        this.editAddAll(source, collStakes, ce);
    }
