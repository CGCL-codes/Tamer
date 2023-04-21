    protected void transfer(InputStream in, OutputStream out) throws IOException {
        if (this.blockSize == 1) transferByteByByte(in, out); else if (in instanceof FileInputStream) transferFromFile(((FileInputStream) in).getChannel(), IOChannels.asWritableByteChannel(out)); else if (out instanceof FileOutputStream) transferToFile(IOChannels.asReadableByteChannel(in), ((FileOutputStream) out).getChannel()); else transferDefault(in, out);
        out.flush();
    }
