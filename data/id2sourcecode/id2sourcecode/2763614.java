    protected void createLine() throws LineUnavailableException {
        log.info("Create Line");
        if (m_line == null) {
            AudioFormat sourceFormat = m_audioInputStream.getFormat();
            log.info("Create Line : Source format : " + sourceFormat.toString());
            int nSampleSizeInBits = sourceFormat.getSampleSizeInBits();
            if (nSampleSizeInBits <= 0) {
                nSampleSizeInBits = 16;
            }
            if ((sourceFormat.getEncoding() == AudioFormat.Encoding.ULAW) || (sourceFormat.getEncoding() == AudioFormat.Encoding.ALAW)) {
                nSampleSizeInBits = 16;
            }
            if (nSampleSizeInBits != 8) {
                nSampleSizeInBits = 16;
            }
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), false);
            log.info("Create Line : Target format: " + targetFormat);
            m_encodedaudioInputStream = m_audioInputStream;
            try {
                encodedLength = m_encodedaudioInputStream.available();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Cannot get m_encodedaudioInputStream.available()", e);
            }
            m_audioInputStream = AudioSystem.getAudioInputStream(targetFormat, m_audioInputStream);
            AudioFormat audioFormat = m_audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, -1);
            Mixer mixer = getMixer(m_mixerName);
            if (mixer != null) {
                log.info("Mixer : " + mixer.getMixerInfo().toString());
                m_line = (SourceDataLine) mixer.getLine(info);
            } else {
                m_line = (SourceDataLine) AudioSystem.getLine(info);
                m_mixerName = null;
            }
            log.info("Line : " + m_line.toString());
            log.info("Line Info : " + m_line.getLineInfo().toString());
            log.info("Line AudioFormat: " + m_line.getFormat().toString());
        }
    }
