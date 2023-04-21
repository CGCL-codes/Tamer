        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readAvailable());
            return true;
        }
