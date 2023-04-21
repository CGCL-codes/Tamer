    public void play(File f) {
        this.f = f;
        shouldRun = true;
        System.out.println("=========================> PLAY START");
        boolean bInterpretFilenameAsUrl = false;
        boolean bForceConversion = false;
        boolean bBigEndian = false;
        int nSampleSizeInBits = 16;
        String strMixerName = null;
        int nExternalBufferSize = EXTERNAL_BUFFER_SIZE;
        int nInternalBufferSize = AudioSystem.NOT_SPECIFIED;
        AudioFormat audioFormat = null;
        for (int i = 0; i < AudioSystem.getAudioFileTypes().length; i++) System.out.println(AudioSystem.getAudioFileTypes()[i].getExtension());
        try {
            audioInputStream = AudioSystem.getAudioInputStream(f);
            audioFormat = audioInputStream.getFormat();
        } catch (Exception e) {
            e.toString();
        }
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
            System.exit(1);
        }
        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[nExternalBufferSize];
        while (nBytesRead != -1 && shouldRun) {
            if (pause) try {
                setMute(true);
                Thread.sleep(1000);
                setMute(false);
                continue;
            } catch (Exception e) {
            }
            ;
            try {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = line.write(abData, 0, nBytesRead);
            }
        }
        setMute(true);
        line.drain();
        line.close();
        System.out.println("=========================> PLAY STOP");
        return;
    }
