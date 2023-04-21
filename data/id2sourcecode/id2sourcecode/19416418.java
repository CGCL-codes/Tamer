    @Override
    public void flush() throws IOException {
        if (null == _wrCache) return;
        final int writeLen = _wrCache.curLen;
        if (writeLen <= 0) return;
        final ByteBuffer bb = ByteBuffer.wrap(_wrCache.buf, 0, writeLen);
        final int writtenLen = getChannel().write(bb);
        if (writtenLen != writeLen) throw new IOException("Flush cache write mismatch (" + writeLen + " <> " + writtenLen + ")");
        _wrCache.reset();
        super.flush();
    }
