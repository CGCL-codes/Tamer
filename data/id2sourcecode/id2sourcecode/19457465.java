        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            connection.setAutoflush(true);
            writer = new WriteProcessor(connection);
            Thread t = new Thread(writer);
            t.start();
            return true;
        }
