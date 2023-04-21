    public String toString(String charset) throws IOException {
        index = 0;
        cursor = 0;
        StringBuilder sb = new StringBuilder();
        StringOutputStream sos = new StringOutputStream(sb, charset);
        byte c;
        while ((c = (byte) this.read()) != -1) sos.write(c);
        Streams.safeFlush(sos);
        Streams.safeClose(sos);
        return sb.toString();
    }
