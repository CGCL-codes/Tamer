        public boolean onData(INonBlockingConnection connection) throws IOException {
            onDataCalled.set(true);
            int available = connection.available();
            if (available > 0) {
                connection.write(connection.readByteBufferByLength(available));
            }
            return isReturningTrue;
        }
