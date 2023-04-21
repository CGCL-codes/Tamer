    public char sayDateTime(long time, String escapeDigits, String format, String timezone) throws AgiException {
        return getChannel().sayDateTime(time, escapeDigits, format, timezone);
    }
