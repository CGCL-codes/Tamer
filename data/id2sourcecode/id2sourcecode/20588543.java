    private void overwriteRangeFrom(AudioTrail srcTrail, long srcStart, AudioStake writeStake, long insertPos, long len, int bufLen, int[] trackMap, double progWeight, BlendContext bcPre, BlendContext bcPost) throws IOException, InterruptedException {
        final float[][] srcBuf = new float[srcTrail == null ? 0 : srcTrail.getChannelNum()][];
        final float[][] mappedSrcBuf = new float[this.getChannelNum()][];
        final long preLen = bcPre == null ? 0L : bcPre.getLen();
        final long postLen = bcPost == null ? 0L : bcPost.getLen();
        final float[][] mixBuf = new float[this.getChannelNum()][bufLen];
        final float[][] srcFadeBuf = new float[this.getChannelNum()][];
        final float[][] thisDryBuf = new float[this.getChannelNum()][];
        final float[][] thisFadeBuf = new float[this.getChannelNum()][];
        final float[][] compositeBuf = new float[this.getChannelNum()][];
        boolean srcUsed = false;
        int chunkLen, chunkLen2, deltaChunk;
        int clrMixFadeStart = 0;
        int clrMixFadeStop = bufLen;
        Span chunkSpan;
        long newSrcStart, newInsPos, fadeOff;
        boolean xFadeBegin, xFadeEnd, xFade;
        for (int i = 0; i < trackMap.length; i++) {
            if (trackMap[i] >= 0) {
                if (srcBuf[trackMap[i]] == null) {
                    srcBuf[trackMap[i]] = new float[bufLen];
                    srcUsed = true;
                    srcFadeBuf[i] = srcBuf[trackMap[i]];
                }
                mappedSrcBuf[i] = srcBuf[trackMap[i]];
                compositeBuf[i] = mappedSrcBuf[i];
                thisFadeBuf[i] = mixBuf[i];
            } else {
                thisDryBuf[i] = mixBuf[i];
                compositeBuf[i] = mixBuf[i];
            }
        }
        for (long framesWritten = 0; framesWritten < len; ) {
            chunkLen = (int) Math.min(bufLen, len - framesWritten);
            xFadeBegin = framesWritten < preLen;
            xFadeEnd = len - (framesWritten + chunkLen) < postLen;
            xFade = xFadeBegin || xFadeEnd;
            if (srcUsed) {
                newSrcStart = srcStart + chunkLen;
                chunkSpan = new Span(srcStart, newSrcStart);
                srcTrail.readFrames(srcBuf, 0, chunkSpan);
                srcStart = newSrcStart;
                if (xFadeBegin) {
                    bcPre.fadeIn(framesWritten, srcFadeBuf, 0, srcFadeBuf, 0, (int) Math.min(chunkLen, preLen - framesWritten));
                }
                if (xFadeEnd) {
                    fadeOff = framesWritten - (len - postLen);
                    if (fadeOff < 0) {
                        deltaChunk = (int) -fadeOff;
                        fadeOff = 0;
                    } else {
                        deltaChunk = 0;
                    }
                    chunkLen2 = (int) Math.min(chunkLen, len - framesWritten) - deltaChunk;
                    bcPost.fadeOut(fadeOff, srcFadeBuf, deltaChunk, srcFadeBuf, deltaChunk, chunkLen2);
                }
            }
            newInsPos = insertPos + chunkLen;
            chunkSpan = new Span(insertPos, newInsPos);
            if (xFade) {
                this.readFrames(mixBuf, 0, chunkSpan);
                if (xFadeBegin) {
                    chunkLen2 = (int) Math.min(chunkLen, preLen - framesWritten);
                    deltaChunk = chunkLen - chunkLen2;
                    bcPre.fadeOut(framesWritten, thisFadeBuf, 0, thisFadeBuf, 0, chunkLen2);
                    clrMixFadeStart = chunkLen2;
                    clrMixFadeStop = chunkLen;
                }
                if (xFadeEnd) {
                    fadeOff = framesWritten - (len - postLen);
                    if (fadeOff < 0) {
                        deltaChunk = (int) -fadeOff;
                        fadeOff = 0;
                    } else {
                        deltaChunk = 0;
                    }
                    chunkLen2 = (int) Math.min(chunkLen, len - framesWritten) - deltaChunk;
                    bcPost.fadeIn(fadeOff, thisFadeBuf, deltaChunk, thisFadeBuf, deltaChunk, chunkLen2);
                    clrMixFadeStop = deltaChunk;
                }
                chunkLen2 = clrMixFadeStop - clrMixFadeStart;
                if (chunkLen2 > 0) {
                    for (int i = 0; i < thisFadeBuf.length; i++) {
                        if (thisFadeBuf[i] != null) clear(thisFadeBuf[i], clrMixFadeStart, chunkLen2);
                    }
                }
                clrMixFadeStart = 0;
                clrMixFadeStop = bufLen;
                for (int i = 0; i < mixBuf.length; i++) {
                    if (mappedSrcBuf[i] != null) add(mixBuf[i], 0, mappedSrcBuf[i], 0, chunkLen);
                }
                writeStake.writeFrames(mixBuf, 0, chunkSpan);
            } else {
                this.readFrames(thisDryBuf, 0, chunkSpan);
                writeStake.writeFrames(compositeBuf, 0, chunkSpan);
            }
            framesWritten += chunkLen;
            insertPos = newInsPos;
            setProgression(framesWritten, progWeight);
        }
        writeStake.flush();
    }
