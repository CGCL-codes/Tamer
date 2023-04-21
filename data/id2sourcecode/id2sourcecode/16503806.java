    char nextChar() throws IOException {
        if (previousChar == EOL) {
            throw new IOException("Unexpected end of file");
        }
        if (readIndex + (BUFFER_SIZE - maxPeek) == writeIndex && (int) buf[getIndex(writeIndex)] != -1) {
            fillBuffer();
        }
        char c = buf[getIndex(++readIndex)];
        if (c == '\n' && previousChar != '\r') {
            lineNo++;
            column = 0;
        } else if (c == '\r') {
            lineNo++;
            column = 0;
        } else {
            column++;
        }
        previousChar = c;
        return c;
    }
