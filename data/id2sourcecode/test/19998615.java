    public final void cdata(final InputStream in) throws IOException {
        final byte[] buf = new byte[2048];
        int readBytes;
        if (isOpenStartTag()) closeStartTag();
        out.print("<![CDATA[");
        do {
            readBytes = in.read(buf);
            if (readBytes != -1) out.write(encodeHex(buf, 0, readBytes));
        } while (readBytes != -1);
        out.print("]]>");
    }
