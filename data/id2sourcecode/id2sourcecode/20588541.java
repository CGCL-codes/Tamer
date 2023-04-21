    private void insertRangeFrom(AudioTrail srcTrail, final long srcStart, AudioStake writeStake, final long insertPos, final long len, final int bufLen, int[] trackMap, double progWeight, BlendContext bcPre, BlendContext bcPost) throws IOException, InterruptedException {
        final float[][] srcBuf = new float[srcTrail == null ? 0 : srcTrail.getChannelNum()][];
        final float[][] mappedSrcBuf = new float[this.getChannelNum()][];
        final long preLen = bcPre == null ? 0L : bcPre.getLen();
        final long postLen = bcPost == null ? 0L : bcPost.getLen();
        final float[][] mixBuf = new float[this.getChannelNum()][bufLen];
        final float[][] srcFadeBuf = new float[this.getChannelNum()][];
        final long fadeOutOffset = insertPos - len;
        float[] empty = null;
        boolean srcUsed = false;
        boolean writeMix = false;
        int chunkLen, chunkLen2, deltaChunk;
        int cpToMixStart = 0;
        int cpToMixStop = bufLen;
        Span chunkSpan, chunkSpan2;
        for (int i = 0; i < trackMap.length; i++) {
            if (trackMap[i] >= 0) {
                if (srcBuf[trackMap[i]] == null) {
                    srcBuf[trackMap[i]] = new float[bufLen];
                    srcUsed = true;
                    srcFadeBuf[i] = srcBuf[trackMap[i]];
                }
                mappedSrcBuf[i] = srcBuf[trackMap[i]];
            } else {
                if (empty == null) empty = new float[bufLen];
                mappedSrcBuf[i] = empty;
            }
        }
        for (long framesWritten = 0, remaining = len; remaining > 0; ) {
            chunkLen = (int) Math.min(bufLen, remaining);
            if (srcUsed) {
                chunkSpan = new Span(srcStart + framesWritten, srcStart + framesWritten + chunkLen);
                srcTrail.readFrames(srcBuf, 0, chunkSpan);
                if (framesWritten < preLen) {
                    bcPre.fadeIn(framesWritten, srcFadeBuf, 0, srcFadeBuf, 0, (int) Math.min(chunkLen, preLen - framesWritten));
                }
                if (remaining - chunkLen < postLen) {
                    deltaChunk = (int) Math.max(0, remaining - postLen);
                    chunkLen2 = chunkLen - deltaChunk;
                    bcPost.fadeOut(postLen - remaining + deltaChunk, srcFadeBuf, deltaChunk, srcFadeBuf, deltaChunk, chunkLen2);
                }
            }
            chunkSpan = new Span(insertPos + framesWritten, insertPos + framesWritten + chunkLen);
            if (framesWritten < preLen) {
                chunkLen2 = (int) Math.min(chunkLen, preLen - framesWritten);
                deltaChunk = chunkLen - chunkLen2;
                chunkSpan2 = deltaChunk > 0 ? chunkSpan.replaceStop(chunkSpan.stop - deltaChunk) : chunkSpan;
                this.readFrames(mixBuf, 0, chunkSpan2);
                bcPre.fadeOut(framesWritten, mixBuf, 0, mixBuf, 0, chunkLen2);
                for (int i = 0; i < mixBuf.length; i++) {
                    if (mappedSrcBuf[i] != empty) add(mixBuf[i], 0, mappedSrcBuf[i], 0, chunkLen2);
                }
                cpToMixStart = chunkLen2;
                cpToMixStop = chunkLen;
                writeMix = true;
            }
            if (remaining - chunkLen < postLen) {
                deltaChunk = (int) Math.max(0, remaining - postLen);
                chunkLen2 = chunkLen - deltaChunk;
                chunkSpan2 = new Span(fadeOutOffset + framesWritten + deltaChunk, fadeOutOffset + framesWritten + chunkLen);
                this.readFrames(mixBuf, deltaChunk, chunkSpan2);
                bcPost.fadeIn(postLen - remaining + deltaChunk, mixBuf, deltaChunk, mixBuf, deltaChunk, chunkLen2);
                for (int i = 0; i < mixBuf.length; i++) {
                    if (mappedSrcBuf[i] != empty) add(mixBuf[i], deltaChunk, mappedSrcBuf[i], deltaChunk, chunkLen2);
                }
                cpToMixStop = deltaChunk;
                writeMix = true;
            }
            if (writeMix) {
                chunkLen2 = cpToMixStop - cpToMixStart;
                if (chunkLen2 > 0) {
                    for (int i = 0; i < mixBuf.length; i++) {
                        System.arraycopy(mappedSrcBuf[i], cpToMixStart, mixBuf[i], cpToMixStart, chunkLen2);
                    }
                }
                writeStake.writeFrames(mixBuf, 0, chunkSpan);
                writeMix = false;
                cpToMixStart = 0;
                cpToMixStop = bufLen;
            } else {
                writeStake.writeFrames(mappedSrcBuf, 0, chunkSpan);
            }
            framesWritten += chunkLen;
            remaining -= chunkLen;
            setProgression(framesWritten, progWeight);
        }
        writeStake.flush();
    }
