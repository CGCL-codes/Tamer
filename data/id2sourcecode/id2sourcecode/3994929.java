    protected void openImpl() {
        if (TDebug.TraceSourceDataLine) {
            TDebug.out("EsdSourceDataLine.openImpl(): called.");
        }
        checkOpen();
        AudioFormat format = getFormat();
        AudioFormat.Encoding encoding = format.getEncoding();
        boolean bBigEndian = format.isBigEndian();
        m_bSwapBytes = false;
        if (format.getSampleSizeInBits() == 16 && bBigEndian) {
            m_bSwapBytes = true;
            bBigEndian = false;
        } else if (format.getSampleSizeInBits() == 8 && encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            m_bSwapBytes = true;
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;
        }
        if (System.getProperty("os.arch").equals("ppc") && format.getSampleSizeInBits() == 16) {
            m_bSwapBytes ^= true;
        }
        if (m_bSwapBytes) {
            format = new AudioFormat(encoding, format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), bBigEndian);
            m_nBytesPerSample = format.getFrameSize() / format.getChannels();
        }
        int nOutFormat = Esd.ESD_STREAM | Esd.ESD_PLAY | EsdUtils.getEsdFormat(format);
        m_esdStream = new EsdStream();
        m_esdStream.open(nOutFormat, (int) format.getSampleRate());
    }
