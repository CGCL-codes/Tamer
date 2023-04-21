    public final String getCurrentDataAsString() {
        StringBuffer buf = new StringBuffer("");
        for (HardwareData data : currentData) buf.append("[CH-" + data.getChannel() + "] " + data.getDataString());
        return buf.toString();
    }
