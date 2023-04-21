    private void analyzeWaveform(final AudioWaveformData waveData) {
        try {
            File f = new File(waveData.fileName);
            AudioFileFormat aFormat = AudioSystem.getAudioFileFormat(f);
            AudioFormat format = aFormat.getFormat();
            int numChannels = format.getChannels();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(f)));
            int sr = (int) format.getSampleRate();
            int numBytesPerSample = audioInputStream.getFormat().getSampleSizeInBits() / 8;
            int numFramesToRead = sr / waveData.pixelSeconds;
            boolean bigEndian = format.isBigEndian();
            int len = format.getFrameSize() * numFramesToRead;
            byte[] dataBuffer = new byte[len];
            int maxWidth = (aFormat.getFrameLength() / numFramesToRead) + 1;
            waveData.data = new double[numChannels][maxWidth * 2];
            for (int i = 0; i < maxWidth && running; i++) {
                int numRead = audioInputStream.read(dataBuffer, 0, len);
                if (numRead <= 0) {
                    waveData.percentLoadingComplete = 1.0;
                    break;
                } else {
                    waveData.percentLoadingComplete = i / maxWidth;
                    if (i % 100 == 0) {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                audioWaveformCache.fireAudioWaveformDataGenerated(waveData.fileName);
                            }
                        });
                    }
                }
                prepareSamples(waveData, dataBuffer, i, numChannels, numBytesPerSample, bigEndian);
            }
            waveData.percentLoadingComplete = 1.0;
            audioInputStream.close();
        } catch (UnsupportedAudioFileException e) {
            waveData.data = null;
            e.printStackTrace();
        } catch (IOException e) {
            waveData.data = null;
            e.printStackTrace();
        }
    }
