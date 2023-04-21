    public CsvWriter(File _f) throws IOException {
        out_ = new FileOutputStream(_f, false);
        ch_ = out_.getChannel();
        buf_ = ByteBuffer.allocate(8192);
        charBuf_ = CharBuffer.allocate(8192);
        encoder_ = Charset.forName(System.getProperty("file.encoding")).newEncoder();
    }
