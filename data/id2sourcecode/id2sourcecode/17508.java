    public SingleChannelStereoAudioInputStream(AudioInputStream sourceStream, boolean bSignalOnLeftChannel) {
        super(new ByteArrayInputStream(new byte[0]), new AudioFormat(sourceStream.getFormat().getSampleRate(), sourceStream.getFormat().getSampleSizeInBits(), 2, sourceStream.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED), sourceStream.getFormat().isBigEndian()), sourceStream.getFrameLength());
        if (DEBUG) {
            out("SingleChannelStereoAudioInputStream.<init>(): begin");
        }
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (!AudioCommon.isPcm(sourceFormat.getEncoding())) {
            throw new IllegalArgumentException("source stream has to be PCM");
        }
        if (sourceFormat.getChannels() != 1) {
            AudioFormat monoFormat = new AudioFormat(sourceFormat.getEncoding(), sourceFormat.getSampleRate(), sourceFormat.getSampleSizeInBits(), 1, (sourceFormat.getSampleSizeInBits() + 7) / 8, sourceFormat.getFrameRate(), sourceFormat.isBigEndian());
            sourceStream = AudioSystem.getAudioInputStream(monoFormat, sourceStream);
        }
        m_sourceStream = sourceStream;
        m_bSignalOnLeftChannel = bSignalOnLeftChannel;
        int nSampleSizeInBytes = getFormat().getFrameSize() / 2;
        m_abSilenceSample = new byte[nSampleSizeInBytes];
        if (getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            if (getFormat().getSampleSizeInBits() == 8) m_abSilenceSample[0] = (byte) 128; else throw new IllegalArgumentException("unsigned formats are only supported for 8 bit");
        }
        if (DEBUG) {
            out("SingleChannelStereoAudioInputStream.<init>(): end");
        }
    }
