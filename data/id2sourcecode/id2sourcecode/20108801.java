    public void openAudioFile(String filename) {
        this.filename = filename;
        File soundFile = new File(filename);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioStream.getFormat();
            baseFreq = (double) audioFormat.getSampleRate();
            channels = audioFormat.getChannels();
            int avail = audioStream.available();
            int frameSize = audioFormat.getFrameSize();
            byte[] samples = new byte[avail];
            int r = audioStream.read(samples, 0, avail);
            streamLength = avail / frameSize;
            sampleMemory = new double[streamLength];
            for (int i = 0; i < streamLength; i++) {
                int ptr = i * frameSize;
                int leftValue = samples[ptr + 0];
                leftValue += samples[ptr + 1] << 8;
                int rightValue = samples[ptr + 2];
                rightValue += samples[ptr + 3] << 8;
                int nValue = (leftValue + rightValue) / 2;
                sampleMemory[i] = ((double) nValue / (16 * 16 * 16 * 16));
            }
            DebugTools.msg(70, "opened " + filename + ": " + streamLength + " words");
            playbackStart = 0.0;
            loopStart = 0.0;
            loopEnd = streamLength;
            playbackMode = MODE_TRIGGER;
            audioStream.close();
        } catch (Exception e) {
            return;
        }
    }
