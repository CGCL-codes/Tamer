        private final int transferFrom(ReadableByteChannel contentSource, int transferChunkSize) throws IOException, ClosedConnectionException {
            int transfered = 0;
            int read = 0;
            do {
                ByteBuffer transferBuffer = ByteBuffer.allocate(transferChunkSize);
                read = contentSource.read(transferBuffer);
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
