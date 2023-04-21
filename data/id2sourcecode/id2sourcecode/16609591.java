    private void fillBuffer() throws IOException {
        Token tok;
        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
            writeIndex++;
            buf[getIndex(writeIndex)] = tok;
            if (readIndex == -1) {
                if (writeIndex == BUFFER_SIZE - 1) break;
            } else {
                if (writeIndex - readIndex == maxPeek) break;
            }
        }
    }
