    protected int available() {
        if (writeIndex == readIndex) {
            return 0;
        } else if (writeIndex > readIndex) {
            return writeIndex - readIndex;
        } else {
            return buf.length - readIndex + writeIndex;
        }
    }
