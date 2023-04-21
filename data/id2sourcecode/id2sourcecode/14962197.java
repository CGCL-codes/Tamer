    private int available() {
        if (readPosition <= writePosition) {
            return (writePosition - readPosition);
        } else {
            return (buffer.length - (readPosition - writePosition));
        }
    }
