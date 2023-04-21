        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            threadName = Thread.currentThread().getName();
            countOnData++;
            connection.write(connection.readStringByDelimiter(DELIMITER) + DELIMITER);
            return true;
        }
