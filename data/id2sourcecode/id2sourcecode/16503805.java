    private void fillBuffer() throws IOException {
        int c;
        while (true) {
            c = reader.read();
            writeIndex++;
            buf[getIndex(writeIndex)] = (char) c;
            if (readIndex == -1) {
                if (c == -1 || writeIndex == BUFFER_SIZE - 1) break;
            } else {
                if (c == -1 || writeIndex - readIndex == maxPeek) break;
            }
        }
    }
