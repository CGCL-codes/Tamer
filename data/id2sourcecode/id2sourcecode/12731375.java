        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            connection.write(connection.readAvailableByteBuffer());
            return true;
        }
