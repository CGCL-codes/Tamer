    private static String getAudioFormatStringImpl0(AudioFormat audioFormat) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(audioFormat.getEncoding().toString());
        strBuf.append(", ");
        strBuf.append(audioFormat.getSampleRate());
        strBuf.append(" Hz , ");
        strBuf.append(audioFormat.getSampleSizeInBits());
        strBuf.append(" bit , ");
        strBuf.append(audioFormat.getChannels());
        strBuf.append(" ch, ");
        strBuf.append(audioFormat.getFrameSize());
        strBuf.append(" byte, ");
        strBuf.append(audioFormat.getFrameRate());
        strBuf.append(" Hz, ");
        strBuf.append(audioFormat.isBigEndian() ? "BE" : "le");
        return strBuf.toString();
    }
