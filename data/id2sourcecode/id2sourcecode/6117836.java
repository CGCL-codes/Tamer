        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            threadName = Thread.currentThread().getName();
            connection.write(connection.readStringByDelimiter(DELIMITER) + DELIMITER);
            return true;
        }
