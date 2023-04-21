    public char recordFile(String file, String format, String escapeDigits, int timeout, int offset, boolean beep, int maxSilence) throws AgiException {
        return getChannel().recordFile(file, format, escapeDigits, timeout, offset, beep, maxSilence);
    }
