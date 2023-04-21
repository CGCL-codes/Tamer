    public char controlStreamFile(String file, String escapeDigits, int offset, String forwardDigit, String rewindDigit, String pauseDigit) throws AgiException {
        return getChannel().controlStreamFile(file, escapeDigits, offset, forwardDigit, rewindDigit, pauseDigit);
    }
