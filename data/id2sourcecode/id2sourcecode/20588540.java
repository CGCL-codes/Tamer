    private void overwriteRangeFrom(AudioTrail srcTrail, long srcStart, AudioStake writeStake, long insertPos, long len, int bufLen, int[] trackMap, double progWeight) throws IOException, InterruptedException {
        final float[][] srcBuf = new float[srcTrail == null ? 0 : srcTrail.getChannelNum()][];
        final float[][] outBuf = new float[this.getChannelNum()][];
        final float[][] thisBuf = new float[this.getChannelNum()][];
        boolean srcUsed = false;
        boolean thisUsed = false;
        int chunkLen;
        Span chunkSpan;
        long newSrcStart, newInsPos;
        for (int i = 0; i < trackMap.length; i++) {
            if (trackMap[i] >= 0) {
                if (srcBuf[trackMap[i]] == null) {
                    srcBuf[trackMap[i]] = new float[bufLen];
                    srcUsed = true;
                }
                outBuf[i] = srcBuf[trackMap[i]];
            } else {
                thisBuf[i] = new float[bufLen];
                outBuf[i] = thisBuf[i];
                thisUsed = true;
            }
        }
        for (long framesWritten = 0; framesWritten < len; ) {
            chunkLen = (int) Math.min(bufLen, len - framesWritten);
            if (srcUsed) {
                newSrcStart = srcStart + chunkLen;
                chunkSpan = new Span(srcStart, newSrcStart);
                srcTrail.readFrames(srcBuf, 0, chunkSpan);
                srcStart = newSrcStart;
            }
            newInsPos = insertPos + chunkLen;
            chunkSpan = new Span(insertPos, newInsPos);
            if (thisUsed) {
                this.readFrames(thisBuf, 0, chunkSpan);
            }
            writeStake.writeFrames(outBuf, 0, chunkSpan);
            framesWritten += chunkLen;
            insertPos = newInsPos;
            setProgression(framesWritten, progWeight);
        }
        writeStake.flush();
    }
