    private void mixRangeFrom(AudioTrail srcTrail, long srcStart, AudioStake writeStake, long insertPos, long len, int bufLen, int[] trackMap, double progWeight, BlendContext bcPre, BlendContext bcPost) throws IOException, InterruptedException {
        final float[][] srcBuf = new float[srcTrail == null ? 0 : srcTrail.getChannelNum()][];
        final float[][] mappedSrcBuf = new float[this.getChannelNum()][];
        final long preLen = bcPre == null ? 0L : bcPre.getLen();
        final long postLen = bcPost == null ? 0L : bcPost.getLen();
        final float[][] mixBuf = new float[this.getChannelNum()][bufLen];
        final float[][] srcFadeBuf = new float[this.getChannelNum()][];
        boolean srcUsed = false;
        int chunkLen;
        Span chunkSpan, chunkSpan2;
        long newSrcStart, newInsPos;
        for (int i = 0; i < trackMap.length; i++) {
            if (trackMap[i] >= 0) {
                if (srcBuf[trackMap[i]] == null) {
                    srcBuf[trackMap[i]] = new float[bufLen];
                    srcUsed = true;
                    srcFadeBuf[i] = srcBuf[trackMap[i]];
                }
                mappedSrcBuf[i] = srcBuf[trackMap[i]];
            }
        }
        for (long framesWritten = 0; framesWritten < len; ) {
            chunkLen = (int) Math.min(bufLen, len - framesWritten);
            newInsPos = insertPos + chunkLen;
            chunkSpan = new Span(insertPos, newInsPos);
            this.readFrames(mixBuf, 0, chunkSpan);
            if (srcUsed) {
                newSrcStart = srcStart + chunkLen;
                chunkSpan2 = new Span(srcStart, newSrcStart);
                srcTrail.readFrames(srcBuf, 0, chunkSpan2);
                srcStart = newSrcStart;
                if (framesWritten < preLen) {
                    bcPre.fadeIn(framesWritten, srcFadeBuf, 0, srcFadeBuf, 0, (int) Math.min(chunkLen, preLen - framesWritten));
                }
                if (len - (framesWritten + chunkLen) < postLen) {
                    bcPost.fadeOut(framesWritten - (len - postLen), srcFadeBuf, 0, srcFadeBuf, 0, (int) Math.min(chunkLen, len - framesWritten));
                }
                for (int i = 0; i < mixBuf.length; i++) {
                    if (mappedSrcBuf[i] != null) add(mixBuf[i], 0, mappedSrcBuf[i], 0, chunkLen);
                }
            }
            writeStake.writeFrames(mixBuf, 0, chunkSpan);
            framesWritten += chunkLen;
            insertPos = newInsPos;
            setProgression(framesWritten, progWeight);
        }
        writeStake.flush();
    }
