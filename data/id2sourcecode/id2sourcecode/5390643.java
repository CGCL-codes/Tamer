    public void play(InputStream isSound) {
        try {
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(isSound);
            AudioInputStream ais = AudioSystem.getAudioInputStream(isSound);
            AudioFormat audioFormat;
            AudioInputStream oldAis = null;
            if (OGG_AUDIO_TYPE.equalsIgnoreCase(aff.getType().toString())) {
                AudioFormat baseFormat = ais.getFormat();
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                oldAis = ais;
                ais = AudioSystem.getAudioInputStream(audioFormat, ais);
            } else audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            int nBytesRead = 0;
            byte[] abData = new byte[bufferSize];
            do {
                nBytesRead = ais.read(abData);
                if (nBytesRead > 0) line.write(abData, 0, nBytesRead);
            } while (nBytesRead >= 0 && isStopRequested() == false);
            line.drain();
            line.close();
            ais.close();
            if (oldAis != null) oldAis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
