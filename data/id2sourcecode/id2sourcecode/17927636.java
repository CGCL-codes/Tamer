    public void skip(int n) {
        if (n > availableForGet()) {
            throw new IllegalArgumentException("cannot skip so much");
        }
        readPosition += n;
        if (readPosition == writePosition) {
            readPosition = 0;
            writePosition = 0;
        } else if (readPosition >= data.length) {
            readPosition -= data.length;
            writePosition -= data.length;
        }
    }
