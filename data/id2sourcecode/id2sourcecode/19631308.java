    private void createLine() throws LineUnavailableException {
        if (m_line == null) {
            AudioFormat sourceFormat = m_audioInputStream.getFormat();
            if (LOG.isDebugEnabled()) LOG.debug("Source format : " + sourceFormat);
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            if (LOG.isDebugEnabled()) LOG.debug("Target format: " + targetFormat);
            m_audioInputStream = AudioSystem.getAudioInputStream(targetFormat, m_audioInputStream);
            AudioFormat audioFormat = m_audioInputStream.getFormat();
            if (LOG.isDebugEnabled()) LOG.debug("Create Line : " + audioFormat);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            m_line = (SourceDataLine) AudioSystem.getLine(info);
            Control[] c = m_line.getControls();
            for (int p = 0; p < c.length; p++) {
                if (LOG.isDebugEnabled()) LOG.debug("Controls : " + c[p].toString());
            }
            if (m_line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                m_gainControl = (FloatControl) m_line.getControl(FloatControl.Type.MASTER_GAIN);
                if (LOG.isDebugEnabled()) LOG.debug("Master Gain Control : [" + m_gainControl.getMinimum() + "," + m_gainControl.getMaximum() + "]," + m_gainControl.getPrecision());
            }
            if (m_line.isControlSupported(FloatControl.Type.PAN)) {
                m_panControl = (FloatControl) m_line.getControl(FloatControl.Type.PAN);
                if (LOG.isDebugEnabled()) LOG.debug("Pan Control : [" + m_panControl.getMinimum() + "," + m_panControl.getMaximum() + "]," + m_panControl.getPrecision());
            }
        }
    }
