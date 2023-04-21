    protected void writeHeader(AnalyseReader.Header hdr, String path) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(348);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 348; i++) buffer.put((byte) 0x0);
        buffer.putInt(0, 0x15C);
        buffer.putShort(40, (short) hdr.dim);
        buffer.putShort(42, (short) hdr.width);
        buffer.putShort(44, (short) hdr.height);
        buffer.putShort(46, (short) hdr.depth);
        buffer.putShort(70, (short) hdr.datatype);
        buffer.putShort(72, (short) hdr.bitsPerPixel);
        buffer.putFloat(76, hdr.dim);
        buffer.putFloat(80, hdr.pixdim[0]);
        buffer.putFloat(84, hdr.pixdim[1]);
        buffer.putFloat(88, hdr.pixdim[2]);
        buffer.position(0);
        RandomAccessFile out = new RandomAccessFile(path, "rw");
        out.setLength(0);
        if (out.getChannel().write(buffer) != 348) {
            throw new IOException("invalid written bytes size for header");
        }
        out.close();
    }
