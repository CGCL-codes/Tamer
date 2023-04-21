    public MicReader() {
        try {
            AudioFormat aFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0f, 16, 2, 4, 8000.0f, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, aFormat);
            line = (TargetDataLine) AudioSystem.getLine(info);
            audioInputStream = new AudioInputStream(line);
            line.open(aFormat);
            AudioFormat targetFormat = new AudioFormat(SpeexEncoding.SPEEX_Q0, aFormat.getSampleRate(), -1, aFormat.getChannels(), -1, -1, false);
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
        } catch (Exception e) {
            System.out.println("Exception " + e);
            System.exit(0);
        }
    }
