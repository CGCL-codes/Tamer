    public final void cdata(final Reader reader) throws IOException {
        final char[] buf = new char[4096];
        int readBytes;
        if (isOpenStartTag()) closeStartTag();
        out.print("<![CDATA[");
        do {
            readBytes = reader.read(buf);
            if (readBytes != -1) out.write(buf, 0, readBytes);
        } while (readBytes != -1);
        out.print("]]>");
    }
