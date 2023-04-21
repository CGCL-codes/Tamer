    private void createLine() {
        int channels = stream.getChannels();
        int bits = 32;
        int frame = 0;
        int frequency = stream.getSampleRate();
        frame = channels * (bits / 8);
        zeroData = new byte[frame * 100];
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, frequency / 2, bits, channels, frame, frequency / 2, false);
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            line = (SourceDataLine) mixer.getLine(info);
            line.open(format);
            line.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            try {
                gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (line.isControlSupported(FloatControl.Type.VOLUME)) {
            try {
                volumeControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
