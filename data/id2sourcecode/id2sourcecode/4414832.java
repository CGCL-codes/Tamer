    protected final ChannelBuffer readBlockChannelBuffer() throws ArUnvalidIndexException, ArFileException, ArEndTransferException {
        if (isReady && inputStream != null) {
            byte[] readBytes = readBlock();
            ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(readBytes);
            buffer.writerIndex(readBytes.length);
            return buffer;
        }
        throw new ArUnvalidIndexException("Doc is not ready");
    }
