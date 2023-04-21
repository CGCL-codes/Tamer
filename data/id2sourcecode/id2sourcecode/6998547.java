    public void play() throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audio));
        AudioInputStream din = null;
        AudioFormat audioFormat = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, nInternalBufferSize);
        boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
        if (!bIsSupportedDirectly || bForceConversion) {
            AudioFormat sourceFormat = audioFormat;
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), bBigEndian);
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
            audioFormat = audioInputStream.getFormat();
        }
        line = getSourceDataLine(strMixerName, audioFormat, nInternalBufferSize);
        if (line == null) {
            System.out.println("AudioPlayer: cannot get SourceDataLine for format " + audioFormat);
            return;
        }
        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[nExternalBufferSize];
        while (nBytesRead != -1) {
            if (!running) break;
            try {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = line.write(abData, 0, nBytesRead);
            }
        }
        line.drain();
        line.close();
    }
