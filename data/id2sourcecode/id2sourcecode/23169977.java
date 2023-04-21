        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readByteBufferByLength(connection.available()));
            return true;
        }
