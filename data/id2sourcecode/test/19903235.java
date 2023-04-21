    private int available() {
        if (readPosition <= writePosition) {
            return (writePosition - readPosition);
        }
        return (buffer.length - (readPosition - writePosition));
    }
