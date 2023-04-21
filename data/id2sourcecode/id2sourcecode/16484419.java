    private void setReadIndexToNextLine() {
        while (buf[readIndex] != '\n') {
            readIndex++;
            if (readIndex == buf.length) {
                readIndex = 0;
            }
            if (readIndex == writeIndex) {
                return;
            }
        }
        readIndex++;
        if (readIndex == buf.length) {
            readIndex = 0;
        }
    }
