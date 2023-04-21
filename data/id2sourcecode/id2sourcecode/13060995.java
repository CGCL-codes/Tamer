    public XmlChannelWriter(String filename) throws FileNotFoundException, IOException {
        FileOutputStream dest = new FileOutputStream(filename);
        destC = dest.getChannel();
        bbuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        buffer = CharBuffer.allocate(BUFFER_SIZE);
        encoder = Charset.forName("ISO-8859-1").newEncoder();
        tagStack = new Stack();
        buffer.put(header);
        flush();
    }
