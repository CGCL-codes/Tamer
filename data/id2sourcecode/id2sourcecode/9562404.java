    private final void createLine() throws LineUnavailableException {
        if (m_line == null) {
            final AudioFormat sourceFormat = m_audioInputStream.getFormat();
            final AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            if (isApplet) {
                m_audioInputStream = AppletMpegSPIWorkaround.getAudioInputStream(targetFormat, m_audioInputStream);
            } else {
                m_audioInputStream = AudioSystem.getAudioInputStream(targetFormat, m_audioInputStream);
            }
            final AudioFormat audioFormat = m_audioInputStream.getFormat();
            trace(1, "---", "Create Line : ", audioFormat.toString());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            m_line = (SourceDataLine) AudioSystem.getLine(info);
            if (m_line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                m_gainControl = (FloatControl) m_line.getControl(FloatControl.Type.MASTER_GAIN);
                trace(1, "---", "Master Gain Control : [" + m_gainControl.getMinimum() + "," + m_gainControl.getMaximum() + "]", "" + m_gainControl.getPrecision());
            }
            if (m_line.isControlSupported(FloatControl.Type.PAN)) {
                m_panControl = (FloatControl) m_line.getControl(FloatControl.Type.PAN);
                trace(1, "---", "Pan Control : [" + m_panControl.getMinimum() + "," + m_panControl.getMaximum() + "]", "" + m_panControl.getPrecision());
            }
        }
    }
