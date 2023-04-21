        public long transferFrom(ReadableByteChannel sourceChannel) throws ClosedChannelException, IOException, SocketTimeoutException {
            int chunkSize = (Integer) getOption(SO_SNDBUF);
            long transfered = 0;
            int read = 0;
            do {
                ByteBuffer transferBuffer = ByteBuffer.allocate(chunkSize);
                read = sourceChannel.read(transferBuffer);
                if (read > 0) {
                    if (transferBuffer.remaining() == 0) {
                        transferBuffer.flip();
                        write(transferBuffer);
                    } else {
                        transferBuffer.flip();
                        write(transferBuffer.slice());
                    }
                    transfered += read;
                }
            } while (read > 0);
            return transfered;
        }
