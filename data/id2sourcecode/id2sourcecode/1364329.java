    private List<AudioFormat> getSupportedFormats(int nDirection) {
        if (TDebug.TraceMixer) {
            TDebug.out("AlsaDataLineMixer.getSupportedFormats(): begin");
        }
        if (TDebug.TraceMixer) {
            TDebug.out("AlsaDataLineMixer.getSupportedFormats(): direction: " + nDirection);
        }
        List<AudioFormat> supportedFormats = new ArrayList<AudioFormat>();
        AlsaPcm alsaPcm = null;
        try {
            alsaPcm = new AlsaPcm(getPcmName(), nDirection, 0);
        } catch (Exception e) {
            if (TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
            throw new RuntimeException("cannot open pcm");
        }
        int nReturn;
        AlsaPcmHWParams hwParams = new AlsaPcmHWParams();
        nReturn = alsaPcm.getAnyHWParams(hwParams);
        if (nReturn != 0) {
            TDebug.out("AlsaDataLineMixer.getSupportedFormats(): getAnyHWParams(): " + Alsa.getStringError(nReturn));
            throw new RuntimeException(Alsa.getStringError(nReturn));
        }
        AlsaPcmHWParamsFormatMask formatMask = new AlsaPcmHWParamsFormatMask();
        int nMinChannels = hwParams.getChannelsMin();
        if (TDebug.TraceMixer) {
            TDebug.out("AlsaDataLineMixer.getSupportedFormats(): min channels: " + nMinChannels);
        }
        int nMaxChannels = hwParams.getChannelsMax();
        nMaxChannels = Math.min(nMaxChannels, CHANNELS_LIMIT);
        if (TDebug.TraceMixer) {
            TDebug.out("AlsaDataLineMixer.getSupportedFormats(): max channels: " + nMaxChannels);
        }
        hwParams.getFormatMask(formatMask);
        for (int i = 0; i < 32; i++) {
            if (TDebug.TraceMixer) {
                TDebug.out("AlsaDataLineMixer.getSupportedFormats(): checking ALSA format index: " + i);
            }
            if (formatMask.test(i)) {
                if (TDebug.TraceMixer) {
                    TDebug.out("AlsaDataLineMixer.getSupportedFormats(): ...supported");
                }
                AudioFormat audioFormat = AlsaUtils.getAlsaFormat(i);
                if (TDebug.TraceMixer) {
                    TDebug.out("AlsaDataLineMixer.getSupportedFormats(): adding AudioFormat: " + audioFormat);
                }
                addChanneledAudioFormats(supportedFormats, audioFormat, nMinChannels, nMaxChannels);
            } else {
                if (TDebug.TraceMixer) {
                    TDebug.out("AlsaDataLineMixer.getSupportedFormats(): ...not supported");
                }
            }
        }
        alsaPcm.close();
        if (TDebug.TraceMixer) {
            TDebug.out("AlsaDataLineMixer.getSupportedFormats(): end");
        }
        return supportedFormats;
    }
