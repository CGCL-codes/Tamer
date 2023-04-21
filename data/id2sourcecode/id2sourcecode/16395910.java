        public final int transferFrom(ReadableByteChannel contentSource) throws IOException, ClosedConnectionException {
            int transferChunkSize = getSendBufferSize() - ((int) (getSendBufferSize() * 0.1));
            return transferFrom(contentSource, transferChunkSize);
        }
