    protected String getInputDebug() {
        CharArrayWriter writer = _threadWriter.get();
        if (writer != null) return writer.toString(); else return null;
    }
