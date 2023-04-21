    public int available() throws IOException {
        return writePosition > readPosition ? writePosition - readPosition : (writePosition < readPosition ? buffer.length - readPosition + 1 + writePosition : (writeLaps > readLaps ? buffer.length : 0));
    }
