    public AudioOutputStreamOutput(AudioOutputStream audioOutputStream) {
        super(audioOutputStream.getFormat().getChannels());
        m_audioOutputStream = audioOutputStream;
        m_abBuffer = new byte[audioOutputStream.getFormat().getFrameSize()];
    }
