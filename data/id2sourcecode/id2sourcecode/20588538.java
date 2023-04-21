    private void insertRangeFrom(AudioTrail srcTrail, long srcStart, AudioStake writeStake, long insertPos, long len, int bufLen, int[] trackMap, double progWeight) throws IOException, InterruptedException {
        final float[][] srcBuf = new float[srcTrail == null ? 0 : srcTrail.getChannelNum()][];
        final float[][] mappedSrcBuf = new float[this.getChannelNum()][];
        float[] empty = null;
        boolean srcUsed = false;
        int chunkLen;
        Span chunkSpan;
        long newSrcStart, newInsPos;
        for (int i = 0; i < trackMap.length; i++) {
            if (trackMap[i] >= 0) {
                if (srcBuf[trackMap[i]] == null) {
                    srcBuf[trackMap[i]] = new float[bufLen];
                    srcUsed = true;
                }
                mappedSrcBuf[i] = srcBuf[trackMap[i]];
            } else {
                if (empty == null) empty = new float[bufLen];
                mappedSrcBuf[i] = empty;
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
            writeStake.writeFrames(mappedSrcBuf, 0, chunkSpan);
            framesWritten += chunkLen;
            insertPos = newInsPos;
            setProgression(framesWritten, progWeight);
        }
        writeStake.flush();
    }
