        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            countOnDataCalled++;
            onDataThreadname = Thread.currentThread().getName();
            connection.write((connection.readByteBufferByLength(connection.available())));
            return resultOnData;
        }
