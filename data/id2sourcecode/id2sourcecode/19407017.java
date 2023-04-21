    private int readAvailableBodyChunk(ByteBuffer buf) throws IOException {
        if (bufferedRead) {
            chunkRemaining -= requestBodyPipe.transferFrom(buf, chunkRemaining);
            if (chunkRemaining > 0) {
                bufferedRead = false;
            }
            return MORE_INPUT;
        } else {
            int writeCount = requestBodyPipe.transferFrom(socketChannel, chunkRemaining);
            if (writeCount == 0) return NO_INPUT;
            if (writeCount == -1) return END_OF_INPUT;
            chunkRemaining -= writeCount;
            return MORE_INPUT;
        }
    }
