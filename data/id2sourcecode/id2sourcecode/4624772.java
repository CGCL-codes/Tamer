        private synchronized void resizeBuffers() {
            sourceBufferSizeSamples = (int) AudioUtils.millis2Frames((long) sourceBufferTime, sourceSampleRate);
            if (sourceBufferSizeSamples < minimumSamplesInHistory) {
                sourceBufferSizeSamples = minimumSamplesInHistory;
            }
            if (sourceBufferSizeSamples < outSamples2inSamples(1)) {
                sourceBufferSizeSamples = ((int) outSamples2inSamples(1)) + 1;
            }
            if (historyBuffer == null) {
                historyBuffer = new FloatSampleBuffer(getFormat().getChannels(), sourceBufferSizeSamples, sourceSampleRate);
                historyBuffer.makeSilence();
            }
            historyBuffer.changeSampleCount(sourceBufferSizeSamples, true);
            if (thisBuffer == null) {
                thisBuffer = new FloatSampleBuffer(getFormat().getChannels(), sourceBufferSizeSamples, sourceSampleRate);
            }
            thisBuffer.changeSampleCount(sourceBufferSizeSamples, true);
            if (TDebug.TraceAudioConverter && DEBUG_STREAM) {
                TDebug.out("Initialized thisBuffer and historyBuffer with " + sourceBufferSizeSamples + " samples");
            }
        }
