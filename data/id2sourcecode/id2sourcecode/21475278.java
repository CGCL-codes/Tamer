    public void write(File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        FileChannel chan = stream.getChannel();
        ByteBuffer hdr = ByteBuffer.allocate(DdsHeader.writtenSize());
        hdr.order(ByteOrder.LITTLE_ENDIAN);
        _header.write(hdr);
        hdr.rewind();
        chan.write(hdr);
        _buf.position(DdsHeader.writtenSize());
        chan.write(_buf);
        chan.force(true);
        chan.close();
        stream.close();
    }
