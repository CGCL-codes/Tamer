    private static ParadoxPK loadPKHeader(final ParadoxConnection conn, final File file) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        FileChannel channel = null;
        final FileInputStream fs = new FileInputStream(file);
        final ParadoxPK pk = new ParadoxPK();
        try {
            channel = fs.getChannel();
            channel.read(buffer);
            buffer.flip();
            pk.setName(file.getName());
            pk.setRecordSize(buffer.getShort());
            pk.setHeaderSize(buffer.getShort());
            pk.setType(buffer.get());
            pk.setBlockSize(buffer.get());
            pk.setRowCount(buffer.getInt());
            pk.setUsedBlocks(buffer.getShort());
            pk.setTotalBlocks(buffer.getShort());
            pk.setFirstBlock(buffer.getShort());
            pk.setLastBlock(buffer.getShort());
            buffer.position(0x15);
            pk.setIndexFieldNumber(buffer.get());
            buffer.position(0x38);
            pk.setWriteProtected(buffer.get());
            pk.setVersionId(buffer.get());
        } finally {
            if (channel != null) {
                channel.close();
            }
            fs.close();
        }
        return pk;
    }
