    void nextToken() throws IOException {
        if (readIndex + (BUFFER_SIZE - maxPeek) == writeIndex && buf[getIndex(writeIndex)].getType() != TokenType.EOF) {
            fillBuffer();
        }
        currentToken = buf[getIndex(++readIndex)];
    }
