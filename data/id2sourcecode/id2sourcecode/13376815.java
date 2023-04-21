    public void write(File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        FileChannel chan = stream.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(header.size());
        buf.order(ByteOrder.LITTLE_ENDIAN);
        header.write(buf);
        buf.rewind();
        chan.write(buf);
        chan.write(data);
        data.rewind();
        chan.force(true);
        chan.close();
        stream.close();
    }
