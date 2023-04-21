        @Override
        public long transferTo(WritableByteChannel outputChannel) throws ClosedConnectionException, IOException, SocketTimeoutException, MaxReadSizeExceededException {
            if ((remainingDataLength == 0) && (getNumberOfAvailableBytes() == 0)) {
                return -1;
            }
            int read = 0;
            for (ByteBuffer buffer : HttpConnection.this.readByteBuffer()) {
                read += outputChannel.write(buffer);
            }
            decRemainingDataSize(read);
            return read;
        }
