        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            connection.write(connection.readStringByDelimiter("\r\n") + "\r\n");
            return true;
        }
