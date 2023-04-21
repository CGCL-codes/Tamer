    private ByteBuffer readData(final int idx) {
        final ByteBuffer bf = ByteBuffer.allocate(longueurEnregistrement);
        bf.order(byteOrder);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            final FileChannel channel = fileInputStream.getChannel();
            channel.read(bf, ((long) idx) * longueurEnregistrement);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "read", e);
        } finally {
            CtuluLibFile.close(fileInputStream);
        }
        bf.rewind();
        return bf;
    }
