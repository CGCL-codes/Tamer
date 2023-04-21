    private void flatten(AudioStake target, Span span, int[] channelMap) throws IOException {
        if (span.isEmpty()) return;
        final int outChannels = target.getChannelNum();
        if (channelMap == null) {
            if (outChannels != numChannels) throw new IllegalArgumentException();
            channelMap = new int[outChannels];
            for (int i = 0; i < channelMap.length; i++) {
                channelMap[i] = i;
            }
        } else {
            if (outChannels != channelMap.length) throw new IllegalArgumentException();
            for (int i = 0; i < channelMap.length; i++) {
                if ((channelMap[i] < 0) || (channelMap[i] >= numChannels)) throw new IllegalArgumentException();
            }
        }
        final double progWeight = 1.0 / span.getLength();
        final int num = getNumStakes();
        final float[][] outBuf = new float[outChannels][BUFSIZE];
        final float[][] inBuf = new float[numChannels][];
        int idx = indexOf(span.start, true);
        if (idx < 0) idx = Math.max(0, -(idx + 2));
        long readOff = span.start;
        AudioStake source;
        int chunkLen;
        Span sourceSpan, subSpan;
        long readStop = span.start;
        for (int i = 0; i < channelMap.length; i++) {
            inBuf[channelMap[i]] = outBuf[i];
        }
        while ((readStop < span.stop) && (idx < num)) {
            source = (AudioStake) get(idx, true);
            sourceSpan = source.getSpan();
            readStop = Math.min(sourceSpan.stop, span.stop);
            while (readOff < readStop) {
                chunkLen = (int) Math.min(BUFSIZE, readStop - readOff);
                subSpan = new Span(readOff, readOff + chunkLen);
                source.readFrames(inBuf, 0, subSpan);
                target.writeFrames(outBuf, 0, subSpan);
                readOff += chunkLen;
                setProgression(readOff - span.start, progWeight);
            }
            idx++;
        }
        if (readStop < span.stop) {
            System.err.println("WARNING: trying to flatten beyond the trail's stop");
            for (int ch = 0; ch < outBuf.length; ch++) {
                for (int i = 0; i < outBuf[ch].length; i++) {
                    outBuf[ch][i] = 0f;
                }
            }
            while (readOff < span.stop) {
                chunkLen = (int) Math.min(BUFSIZE, span.stop - readOff);
                subSpan = new Span(readOff, readOff + chunkLen);
                target.writeFrames(outBuf, 0, subSpan);
                readOff += chunkLen;
                setProgression(readOff - span.start, progWeight);
            }
        }
    }
