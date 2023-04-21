    public void open(File file) throws IOException {
        fin = new FileInputStream(file);
        channel = fin.getChannel();
        buffer = new BigByteBuffer2(channel, FileChannel.MapMode.READ_ONLY);
        myHeader = new DbaseFileHeaderNIO();
        myHeader.readHeader(buffer);
        charBuffer = CharBuffer.allocate(myHeader.getRecordLength() - 1);
        String charSetName = DbfEncodings.getInstance().getCharsetForDbfId(myHeader.getLanguageID());
        if (charSetName == null) {
            charSet = defaultCharset;
        } else {
            if (charSetName.equalsIgnoreCase("UNKNOWN")) charSet = defaultCharset; else {
                try {
                    charSet = Charset.forName(charSetName);
                } catch (UnsupportedCharsetException e) {
                    charSet = defaultCharset;
                }
            }
        }
    }
