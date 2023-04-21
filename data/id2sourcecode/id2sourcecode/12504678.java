    @Overrides
    final void toStreamSecure(int fromIndex, int toIndex, DataOutput dest) throws IOException {
        if (toIndex - fromIndex < 16) super.toStreamSecure(fromIndex, toIndex, dest); else if (dest instanceof WritableByteChannel) toByteChannel(fromIndex, toIndex, (WritableByteChannel) dest, true); else if (dest instanceof FileOutputStream) toByteChannel(fromIndex, toIndex, ((FileOutputStream) dest).getChannel(), true); else if (dest instanceof RandomAccessFile) toByteChannel(fromIndex, toIndex, ((RandomAccessFile) dest).getChannel(), true); else super.toStreamSecure(fromIndex, toIndex, dest);
    }
