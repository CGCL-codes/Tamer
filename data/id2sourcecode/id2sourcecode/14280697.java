    public char recordFile(String file, String format, String escapeDigits, int timeout) throws AgiException {
        return getChannel().recordFile(file, format, escapeDigits, timeout);
    }
