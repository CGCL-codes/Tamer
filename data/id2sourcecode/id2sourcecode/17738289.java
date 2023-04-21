    protected void initSourceDataLine() throws PlayerException {
        if (sourceDataLine == null) {
            try {
                logger.info("Create Source Data Line");
                AudioFormat sourceAudioFormat = audioInputStream.getFormat();
                logger.info("Source format: {}", sourceAudioFormat);
                int nSampleSizeInBits = sourceAudioFormat.getSampleSizeInBits();
                if (nSampleSizeInBits <= 0) {
                    nSampleSizeInBits = 16;
                }
                if ((sourceAudioFormat.getEncoding() == AudioFormat.Encoding.ULAW) || (sourceAudioFormat.getEncoding() == AudioFormat.Encoding.ALAW)) {
                    nSampleSizeInBits = 16;
                }
                if (nSampleSizeInBits != 8) {
                    nSampleSizeInBits = 16;
                }
                AudioFormat targetAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceAudioFormat.getSampleRate(), nSampleSizeInBits, sourceAudioFormat.getChannels(), sourceAudioFormat.getChannels() * (nSampleSizeInBits / 8), sourceAudioFormat.getSampleRate(), false);
                logger.info("Target format: {}", targetAudioFormat);
                audioInputStream = AudioSystem.getAudioInputStream(targetAudioFormat, audioInputStream);
                AudioFormat audioFormat = audioInputStream.getFormat();
                DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
                if (!AudioSystem.isLineSupported(lineInfo)) {
                    throw new PlayerException(lineInfo + " is not supported");
                }
                if (mixerName == null) {
                    mixerName = getMixers().get(0);
                }
                Mixer mixer = getMixer(mixerName);
                if (mixer != null) {
                    logger.info("Mixer: " + mixer.getMixerInfo().toString());
                    sourceDataLine = (SourceDataLine) mixer.getLine(lineInfo);
                } else {
                    sourceDataLine = (SourceDataLine) AudioSystem.getLine(lineInfo);
                    mixerName = null;
                }
                sourceDataLine.addLineListener(this);
                logger.info("Line Info: " + sourceDataLine.getLineInfo().toString());
                logger.info("Line AudioFormat: " + sourceDataLine.getFormat().toString());
                if (bufferSize <= 0) {
                    bufferSize = sourceDataLine.getBufferSize();
                }
                sourceDataLine.open(audioFormat, bufferSize);
                logger.info("Line: BufferSize = {}", sourceDataLine.getBufferSize());
                for (Control c : sourceDataLine.getControls()) {
                    logger.info("Controls: {}", c);
                }
                if (sourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    gainControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                }
                if (sourceDataLine.isControlSupported(FloatControl.Type.PAN)) {
                    panControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.PAN);
                }
                if (sourceDataLine.isControlSupported(BooleanControl.Type.MUTE)) {
                    muteControl = (BooleanControl) sourceDataLine.getControl(BooleanControl.Type.MUTE);
                }
                state = INIT;
                notifyEvent(Playback.OPENED);
            } catch (LineUnavailableException ex) {
                throw new PlayerException(ex);
            }
        }
    }
