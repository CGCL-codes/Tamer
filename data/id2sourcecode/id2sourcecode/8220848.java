        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.resetToReadMark();
            connection.markReadPosition();
            int i = connection.readInt();
            int length = connection.readInt();
            if (connection.getNumberOfAvailableBytes() >= length) {
                connection.removeReadMark();
                connection.write(connection.readByteBufferByLength(length));
            }
            connection.flush();
            return true;
        }
