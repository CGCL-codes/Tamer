    private static String getAudioFormatStringImpl1(AudioFormat audioFormat) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("enc: ");
        strBuf.append(audioFormat.getEncoding().toString());
        strBuf.append(", sr: ");
        strBuf.append(audioFormat.getSampleRate());
        strBuf.append(", ss: ");
        strBuf.append(audioFormat.getSampleSizeInBits());
        strBuf.append(", ch: ");
        strBuf.append(audioFormat.getChannels());
        strBuf.append(", fs: ");
        strBuf.append(audioFormat.getFrameSize());
        strBuf.append(", fr: ");
        strBuf.append(audioFormat.getFrameRate());
        strBuf.append(audioFormat.isBigEndian() ? ", BE" : ", le");
        return strBuf.toString();
    }
