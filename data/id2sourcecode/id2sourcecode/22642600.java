        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readBytesByLength(connection.available()));
            return true;
        }
