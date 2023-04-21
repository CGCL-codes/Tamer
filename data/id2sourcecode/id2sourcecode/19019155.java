    private void updateFileInfo() {
        String strNoInfoText = "---";
        if (m_selectedFile == null) {
            m_encodingLabel.setText(strNoInfoText);
            m_sampleRateLabel.setText(strNoInfoText);
            m_bitsPerSampleLabel.setText(strNoInfoText);
            m_channelsLabel.setText(strNoInfoText);
            m_lengthLabel.setText(strNoInfoText);
            return;
        }
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(m_selectedFile);
        } catch (UnsupportedAudioFileException e) {
            if (Debug.getTraceAllExceptions()) {
                Debug.out(e);
            }
        } catch (IOException e) {
            if (Debug.getTraceAllExceptions()) {
                Debug.out(e);
            }
        }
        if (ais != null) {
            AudioFormat format = ais.getFormat();
            m_encodingLabel.setText(format.getEncoding().toString());
            m_sampleRateLabel.setText("" + ((int) format.getSampleRate()) + " Hz");
            m_bitsPerSampleLabel.setText("" + format.getSampleSizeInBits() + " bit");
            String strChannelsText;
            switch(format.getChannels()) {
                case 1:
                    strChannelsText = "mono";
                    break;
                case 2:
                    strChannelsText = "stereo";
                    break;
                default:
                    strChannelsText = "" + format.getChannels() + "channels";
                    break;
            }
            m_channelsLabel.setText(strChannelsText);
            m_lengthLabel.setText((ais.getFrameLength() == AudioSystem.NOT_SPECIFIED) ? "unknown length" : "" + (ais.getFrameLength() / format.getSampleRate()) + " sec.");
        } else {
            m_encodingLabel.setText(strNoInfoText);
            m_sampleRateLabel.setText(strNoInfoText);
            m_bitsPerSampleLabel.setText(strNoInfoText);
            m_channelsLabel.setText(strNoInfoText);
            m_lengthLabel.setText(strNoInfoText);
        }
    }
