        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            threadName = Thread.currentThread().getName();
            try {
                connection.write(connection.readStringByDelimiter(DELIMITER) + DELIMITER);
            } catch (SocketTimeoutException se) {
                se.printStackTrace();
                throw se;
            }
            return true;
        }
