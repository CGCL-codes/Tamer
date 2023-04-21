    public Player(RegionOfAudioData rad) {
        try {
            this.rad = rad;
            format = rad.getAudioDatabase().getPcm().getAudioFormat();
            data = toBytes(rad.getAudioData().getAudioData());
            stop = false;
            vuMeter = new VUMeter();
            bytesPerFrame = (int) ((format.getChannels() * format.getSampleRate()) / framesPerSecond) * (format.getSampleSizeInBits() / 8);
            info = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                throw new Error("Audio format is not supported");
            }
            out = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            throw new Error(e);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
