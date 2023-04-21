        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
            int available = connection.available();
            if (available > 0) {
                connection.write(connection.readByteBufferByLength(available));
            }
            return true;
        }
