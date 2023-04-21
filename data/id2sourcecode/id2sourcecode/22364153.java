        protected boolean readSupport(SocketChannel sc) throws IOException {
            connection.setTimeStamp();
            SocketChannel chan1 = sc;
            SocketChannel chan2 = sc == source_channel ? target_channel : source_channel;
            DirectByteBuffer read_buffer = sc == source_channel ? source_buffer : target_buffer;
            int len = read_buffer.read(DirectByteBuffer.SS_PROXY, chan1);
            if (len == -1) {
                connection.close();
            } else {
                if (read_buffer.position(DirectByteBuffer.SS_PROXY) > 0) {
                    read_buffer.flip(DirectByteBuffer.SS_PROXY);
                    int written = read_buffer.write(DirectByteBuffer.SS_PROXY, chan2);
                    if (chan1 == source_channel) {
                        outward_bytes += written;
                    } else {
                        inward_bytes += written;
                    }
                    if (read_buffer.hasRemaining(DirectByteBuffer.SS_PROXY)) {
                        connection.cancelReadSelect(chan1);
                        connection.requestWriteSelect(chan2);
                    } else {
                        read_buffer.position(DirectByteBuffer.SS_PROXY, 0);
                        read_buffer.limit(DirectByteBuffer.SS_PROXY, read_buffer.capacity(DirectByteBuffer.SS_PROXY));
                    }
                }
            }
            return (len > 0);
        }
