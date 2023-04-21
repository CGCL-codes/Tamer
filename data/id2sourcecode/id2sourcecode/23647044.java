        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readStringByDelimiter("\r\n") + "\r\n");
            return true;
        }
