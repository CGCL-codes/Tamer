    public static void playAudioStream(AudioInputStream audioInputStream) {
        AudioFormat audioFormat = audioInputStream.getFormat();
        System.out.println("Play input audio format=" + audioFormat);
        if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
            System.out.println("Converting audio format to " + newFormat);
            AudioInputStream newStream = AudioSystem.getAudioInputStream(newFormat, audioInputStream);
            audioFormat = newFormat;
            audioInputStream = newStream;
        }
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Play.playAudioStream does not handle this type of audio on this system.");
            return;
        }
        try {
            SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat);
            if (dataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) dataLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(100.0F);
            }
            dataLine.start();
            int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
            byte[] buffer = new byte[bufferSize];
            try {
                int bytesRead = 0;
                while (bytesRead >= 0) {
                    bytesRead = audioInputStream.read(buffer, 0, buffer.length);
                    if (bytesRead >= 0) {
                        dataLine.write(buffer, 0, bytesRead);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Play.playAudioStream draining line.");
            dataLine.drain();
            System.out.println("Play.playAudioStream closing line.");
            dataLine.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
