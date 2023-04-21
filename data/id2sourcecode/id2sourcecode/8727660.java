    public void setFormat(AudioFormat format) {
        AudioFormat.Encoding type = format.getEncoding();
        if (type == AudioFormat.Encoding.ULAW) {
            ulawB.doClick();
        } else if (type == AudioFormat.Encoding.ALAW) {
            alawB.doClick();
        } else if (type == AudioFormat.Encoding.PCM_SIGNED) {
            linrB.doClick();
            signB.doClick();
        } else if (type == AudioFormat.Encoding.PCM_UNSIGNED) {
            linrB.doClick();
            unsignB.doClick();
        }
        float rate = format.getFrameRate();
        if (rate == 8000) {
            rate8B.doClick();
        } else if (rate == 11025) {
            rate11B.doClick();
        } else if (rate == 16000) {
            rate16B.doClick();
        } else if (rate == 22050) {
            rate22B.doClick();
        } else if (rate == 44100) {
            rate44B.doClick();
        }
        switch(format.getSampleSizeInBits()) {
            case 8:
                size8B.doClick();
                break;
            case 16:
                size16B.doClick();
                break;
        }
        if (format.isBigEndian()) {
            bigB.doClick();
        } else {
            litB.doClick();
        }
        if (format.getChannels() == 1) {
            monoB.doClick();
        } else {
            sterB.doClick();
        }
    }
