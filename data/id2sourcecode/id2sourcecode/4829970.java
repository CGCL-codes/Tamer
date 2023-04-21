    private <R extends FCBValueObject.AbstractRes> ByteBuffer readByte(final ResBuilder<R> resBuilder, final long deb) {
        final ByteBuffer bf = createByteBuffer(resBuilder.getTailleEnr());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            final FileChannel channel = fileInputStream.getChannel();
            channel.read(bf, deb);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "readByte", e);
        } finally {
            CtuluLibFile.close(fileInputStream);
        }
        bf.rewind();
        return bf;
    }
