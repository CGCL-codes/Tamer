        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
            connection.write(connection.readAvailable());
            return true;
        }
