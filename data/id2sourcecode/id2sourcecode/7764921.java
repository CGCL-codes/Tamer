        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            if (!syncMode) {
                connection.setFlushmode(FlushMode.ASYNC);
            }
            Writer writer = new Writer(connection, countPackets, packetSize);
            Thread t = new Thread(writer);
            t.start();
            return true;
        }
