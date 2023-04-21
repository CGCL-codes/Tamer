        protected boolean writeSupport(SocketChannel sc) throws IOException {
            SocketChannel chan1 = sc;
            SocketChannel chan2 = sc == source_channel ? target_channel : source_channel;
            DirectByteBuffer read_buffer = sc == source_channel ? target_buffer : source_buffer;
            int written = read_buffer.write(DirectByteBuffer.SS_PROXY, chan1);
            if (chan1 == target_channel) {
                outward_bytes += written;
            } else {
                inward_bytes += written;
            }
            if (read_buffer.hasRemaining(DirectByteBuffer.SS_PROXY)) {
                connection.requestWriteSelect(chan1);
            } else {
                read_buffer.position(DirectByteBuffer.SS_PROXY, 0);
                read_buffer.limit(DirectByteBuffer.SS_PROXY, read_buffer.capacity(DirectByteBuffer.SS_PROXY));
                connection.requestReadSelect(chan2);
            }
            return (written > 0);
        }
