    public int availableForPut() {
        return dataSize - (writePosition - readPosition);
    }
