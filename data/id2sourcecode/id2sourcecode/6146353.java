    protected boolean decodeFrame() throws JavaLayerException {
        try {
            Header h = bitstream.readFrame();
            if (h == null) return false;
            SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);
            if (analyzer == null) analyzer = new Analyzer(output.getSampleFrequency());
            short[] ob = output.getBuffer();
            for (int a = 0; a < output.getBufferLength() / 2; a++) {
                left_samples[a] = ob[a * 2];
                right_samples[a] = ob[a * 2 + 1];
            }
            analyzer.AnalyzeSamples(left_samples, right_samples, output.getBufferLength() / 2, output.getChannelCount());
            bitstream.closeFrame();
        } catch (RuntimeException ex) {
            throw new JavaLayerException("Exception decoding audio frame", ex);
        }
        return true;
    }
