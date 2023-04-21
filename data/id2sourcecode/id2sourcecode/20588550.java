    private void clearRangeIns(Span clearSpan, int mode, Object source, AbstractCompoundEdit ce, boolean[] trackMap, BlendContext bc) throws IOException {
        final long blendLen = bc == null ? 0L : bc.getLen();
        if (blendLen == 0L) return;
        final boolean t1 = trackMap[0];
        for (int i = 1; i < trackMap.length; i++) {
            if (t1 != trackMap[i]) throw new IllegalStateException(getResourceString("errAudioWillLooseSync"));
        }
        final double perDecProgRatio = 0.9;
        final double progRatio = 1.0 / (1.0 + ((1.0 - perDecProgRatio) / perDecProgRatio) * numDepDec);
        final double progWeight = progRatio / blendLen;
        final long left = bc.getLeftLen();
        final long right = bc.getRightLen();
        final Span fadeInSpan = new Span(clearSpan.stop - left, clearSpan.stop + right);
        final Span fadeOutSpan = new Span(clearSpan.start - left, clearSpan.start + right);
        final int bufLen = (int) Math.min(blendLen, BUFSIZE);
        final Span writeSpan = fadeOutSpan;
        final int numCh = this.getChannelNum();
        final float[][] bufA = new float[numCh][bufLen];
        final float[][] bufB = new float[numCh][bufLen];
        AudioStake writeStake = null;
        int chunkLen;
        long n;
        Span chunkSpan;
        boolean success = false;
        try {
            flushProgression();
            writeStake = alloc(writeSpan);
            for (long framesWritten = 0; framesWritten < blendLen; ) {
                chunkLen = (int) Math.min(bufLen, blendLen - framesWritten);
                n = fadeOutSpan.start + framesWritten;
                chunkSpan = new Span(n, n + chunkLen);
                this.readFrames(bufA, 0, chunkSpan);
                n = fadeInSpan.start + framesWritten;
                chunkSpan = new Span(n, n + chunkLen);
                this.readFrames(bufB, 0, chunkSpan);
                bc.blend(framesWritten, bufA, 0, bufB, 0, bufA, 0, chunkLen);
                n = writeSpan.start + framesWritten;
                chunkSpan = new Span(n, n + chunkLen);
                writeStake.writeFrames(bufA, 0, chunkSpan);
                framesWritten += chunkLen;
                setProgression(framesWritten, progWeight);
            }
            writeStake.flush();
            success = true;
        } finally {
            if (!success && (writeStake != null)) writeStake.dispose();
        }
        this.editAdd(source, writeStake, ce);
    }
