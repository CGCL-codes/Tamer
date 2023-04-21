        @SuppressWarnings("cast")
        public void read(FloatSampleBuffer outBuffer, int offset, int count) {
            if (isClosed() || count == 0) {
                outBuffer.setSampleCount(offset, true);
                return;
            }
            if (outBuffer.getChannelCount() != thisBuffer.getChannelCount()) {
                throw new IllegalArgumentException("passed buffer has different channel count");
            }
            if (TDebug.TraceAudioConverter && DEBUG_STREAM) {
                TDebug.out(">SamplerateConverterStream.read(" + count + " samples)");
            }
            FloatSampleBuffer lSourceBuffer = thisBuffer;
            float[] outSamples;
            float[] inSamples;
            float[] history;
            double increment = outSamples2inSamples(1.0);
            int writtenSamples = 0;
            do {
                int inSampleCount = lSourceBuffer.getSampleCount();
                if (((int) dPos) >= inSampleCount || !thisBufferValid) {
                    readFromSourceStream();
                    if (isClosed()) {
                        break;
                    }
                    lSourceBuffer = thisBuffer;
                    inSampleCount = thisBuffer.getSampleCount();
                    if (inSampleCount == 0) {
                        break;
                    }
                }
                int writeCount = count - writtenSamples;
                if (((int) (outSamples2inSamples((double) writeCount) + dPos)) >= inSampleCount) {
                    int lastOutIndex = ((int) (inSamples2outSamples(((double) inSampleCount) - dPos))) + 1;
                    while ((int) (outSamples2inSamples((double) lastOutIndex) + dPos) >= inSampleCount) {
                        lastOutIndex--;
                        if (DEBUG_STREAM) {
                            TDebug.out("--------- Decremented lastOutIndex=" + lastOutIndex);
                        }
                    }
                    if (DEBUG_STREAM_PROBLEMS) {
                        int testLastOutIndex = writeCount - 1;
                        if (DEBUG_STREAM_PROBLEMS) {
                            while ((int) (outSamples2inSamples((double) testLastOutIndex) + dPos) >= inSampleCount) {
                                testLastOutIndex--;
                            }
                        }
                        if (testLastOutIndex != lastOutIndex) {
                            TDebug.out("lastOutIndex wrong: lastOutIndex=" + lastOutIndex + " testLastOutIndex=" + testLastOutIndex + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    }
                    writeCount = lastOutIndex + 1;
                }
                for (int channel = 0; channel < outBuffer.getChannelCount(); channel++) {
                    inSamples = lSourceBuffer.getChannel(channel);
                    outSamples = outBuffer.getChannel(channel);
                    history = historyBuffer.getChannel(channel);
                    switch(conversionAlgorithm) {
                        case SAMPLE_AND_HOLD:
                            convertSampleAndHold2(inSamples, dPos, inSampleCount, increment, outSamples, writtenSamples + offset, writeCount, history, historyBuffer.getSampleCount());
                            break;
                        case LINEAR_INTERPOLATION:
                            convertLinearInterpolation2(inSamples, dPos, inSampleCount, increment, outSamples, writtenSamples + offset, writeCount, history, historyBuffer.getSampleCount());
                            break;
                    }
                }
                writtenSamples += writeCount;
                dPos += outSamples2inSamples((double) writeCount);
            } while (!isClosed() && writtenSamples < outBuffer.getSampleCount());
            if (writtenSamples < count) {
                outBuffer.changeSampleCount(writtenSamples + offset, true);
            }
            if (TDebug.TraceAudioConverter && DEBUG_STREAM) {
                testOutFramesReturned += outBuffer.getSampleCount();
                TDebug.out("< return " + outBuffer.getSampleCount() + "frames. Total=" + testOutFramesReturned + " frames. Read total " + testInFramesRead + " frames from source stream");
            }
        }
