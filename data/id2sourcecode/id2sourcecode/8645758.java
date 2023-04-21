    protected void openImpl() throws LineUnavailableException {
        if (TDebug.TraceSourceDataLine) {
            TDebug.out("AlsaBaseDataLine.openImpl(): begin");
        }
        checkOpen();
        AudioFormat format = getFormat();
        if (TDebug.TraceSourceDataLine) {
            TDebug.out("AlsaBaseDataLine.openImpl(): input format: " + format);
        }
        boolean bHWBigEndian = false;
        AudioFormat.Encoding encoding = format.getEncoding();
        boolean bBigEndian = format.isBigEndian();
        m_bSwapBytes = false;
        if (format.getSampleSizeInBits() == 16 && bBigEndian != bHWBigEndian) {
            m_bSwapBytes = true;
            bBigEndian = bHWBigEndian;
        } else if (format.getSampleSizeInBits() == 8 && encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            m_bSwapBytes = true;
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;
        }
        if (getSwapBytes()) {
            format = new AudioFormat(encoding, format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), bBigEndian);
            if (TDebug.TraceSourceDataLine) {
                TDebug.out("AlsaBaseDataLine.openImpl(): output format: " + format);
            }
            m_nBytesPerSample = format.getFrameSize() / format.getChannels();
        }
        int nAlsaOutFormat = AlsaUtils.getAlsaFormat(format);
        if (TDebug.TraceSourceDataLine) {
            TDebug.out("AlsaBaseDataLine.openImpl(): ALSA output format: " + nAlsaOutFormat);
        }
        if (nAlsaOutFormat == AlsaPcm.SND_PCM_FORMAT_UNKNOWN) {
            throw new IllegalArgumentException("unsupported format");
        }
        try {
            m_alsaPcm = new AlsaPcm(getAlsaDataLineMixer().getPcmName(), getAlsaStreamType(), 0);
        } catch (Exception e) {
            if (TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
            throw new LineUnavailableException();
        }
        int nReturn;
        AlsaPcmHWParams hwParams = new AlsaPcmHWParams();
        nReturn = m_alsaPcm.getAnyHWParams(hwParams);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): getAnyHWParams(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setHWParamsAccess(hwParams, AlsaPcm.SND_PCM_ACCESS_RW_INTERLEAVED);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setHWParamsFormat(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setHWParamsFormat(hwParams, nAlsaOutFormat);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setHWParamsFormat(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setHWParamsChannels(hwParams, format.getChannels());
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setHWParamsChannels(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setHWParamsRateNear(hwParams, (int) format.getSampleRate());
        if (nReturn < 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setHWParamsRateNear(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setHWParamsBufferTimeNear(hwParams, 500000);
        int nBufferTime = nReturn;
        if (nReturn < 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setHWParamsBufferTimeNear(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setHWParamsPeriodTimeNear(hwParams, nBufferTime / 4);
        if (nReturn < 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setHWParamsPeriodTimeNear(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setHWParams(hwParams);
        if (nReturn < 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setHWParams(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        int nChunkSize = hwParams.getPeriodSize(null);
        int nBufferSize = hwParams.getBufferSize();
        if (nChunkSize == nBufferSize) {
            throw new LineUnavailableException("period size is equal to buffer size");
        }
        AlsaPcmSWParams swParams = new AlsaPcmSWParams();
        nReturn = m_alsaPcm.getSWParams(swParams);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): getSWParams(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setSWParamsSleepMin(swParams, 0);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setSWParamsSleepMin(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setSWParamsXrunMode(swParams, AlsaPcm.SND_PCM_XRUN_NONE);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setSWParamsXrunMode(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setSWParamsAvailMin(swParams, nChunkSize);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setSWParamsAvailMin(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        long lStartThreshold = (long) ((double) format.getFrameRate() * 1 / 1000000);
        nReturn = m_alsaPcm.setSWParamsStartThreshold(swParams, (int) lStartThreshold);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setSWParamsStartThreshold(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        long lStopThreshold = (long) (nBufferSize + (double) format.getFrameRate() * 0 / 1000000);
        nReturn = m_alsaPcm.setSWParamsStopThreshold(swParams, (int) lStopThreshold);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setSWParamsStopThreshold(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        nReturn = m_alsaPcm.setSWParams(swParams);
        if (nReturn != 0) {
            TDebug.out("AlsaBaseDataLine.openImpl(): setSWParams(): " + Alsa.getStringError(nReturn));
            throw new LineUnavailableException(Alsa.getStringError(nReturn));
        }
        if (TDebug.TraceSourceDataLine) {
            TDebug.out("AlsaBaseDataLine.openImpl(): end");
        }
    }
