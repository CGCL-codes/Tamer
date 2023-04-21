    public RawBinaryFormatReader(String filename, long offset, long size, boolean isMapped, boolean isReversed) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(filename, "r");
        if (!isMapped) {
            byte b[] = new byte[(int) size];
            raf.read(b, (int) offset, (int) size);
            raf.close();
            m_mbb = ByteBuffer.wrap(b);
        } else {
            MappedByteBuffer buffer = raf.getChannel().map(MapMode.READ_ONLY, offset, size);
            raf.close();
            m_mbb = buffer;
        }
        m_size = size;
        m_isReversed = isReversed;
    }
