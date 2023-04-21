    public void setVolume(Double value) {
        try {
            AudioFormat format = audioStream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                audioStream = AudioSystem.getAudioInputStream(format, audioStream);
            }
            DataLine.Info info = new DataLine.Info(DataLine.class, audioStream.getFormat(), ((int) audioStream.getFrameLength() * format.getFrameSize()));
            DataLine dataLine = (DataLine) AudioSystem.getLine(info);
            dataLine.open();
            dataLine.start();
            FloatControl volume = (FloatControl) dataLine.getControl(FloatControl.Type.VOLUME);
            float dB = (float) (Math.log(value.doubleValue()) / Math.log(10.0) * 20.0);
            volume.setValue(dB);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
