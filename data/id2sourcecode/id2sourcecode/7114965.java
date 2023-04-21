    public void write(File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        FileChannel chan = stream.getChannel();
        ByteBuffer hdr = ByteBuffer.allocate(Header.writtenSize());
        hdr.order(ByteOrder.LITTLE_ENDIAN);
        header.write(hdr);
        hdr.rewind();
        chan.write(hdr);
        buf.position(Header.writtenSize());
        chan.write(buf);
        chan.force(true);
        chan.close();
        stream.close();
    }
