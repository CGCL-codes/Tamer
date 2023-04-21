    private void createLine() throws LineUnavailableException {
        log.info("Create Line");
        if (m_line == null) {
            AudioFormat sourceFormat = m_audioInputStream.getFormat();
            log.info("Create Line : Source format : " + sourceFormat.toString());
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            log.info("Create Line : Target format: " + targetFormat);
            m_encodedaudioInputStream = m_audioInputStream;
            try {
                encodedLength = m_encodedaudioInputStream.available();
            } catch (IOException e) {
                log.error("Cannot get m_encodedaudioInputStream.available()", e);
            }
            m_audioInputStream = AudioSystem.getAudioInputStream(targetFormat, m_audioInputStream);
            AudioFormat audioFormat = m_audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            m_line = (SourceDataLine) AudioSystem.getLine(info);
            Control[] c = m_line.getControls();
            for (int p = 0; p < c.length; p++) {
                log.debug("Controls : " + c[p].toString());
            }
            if (m_line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                m_gainControl = (FloatControl) m_line.getControl(FloatControl.Type.MASTER_GAIN);
                log.info("Master Gain Control : [" + m_gainControl.getMinimum() + "," + m_gainControl.getMaximum() + "] " + m_gainControl.getPrecision());
            }
            if (m_line.isControlSupported(FloatControl.Type.PAN)) {
                m_panControl = (FloatControl) m_line.getControl(FloatControl.Type.PAN);
                log.info("Pan Control : [" + m_panControl.getMinimum() + "," + m_panControl.getMaximum() + "] " + m_panControl.getPrecision());
            }
        }
    }
