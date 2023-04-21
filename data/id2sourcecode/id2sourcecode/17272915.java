    public boolean writeToNetworkNoFlip() {
        if (!_byteBuffer.hasRemaining()) {
            throw new RuntimeException("Invariant broken: cleaning empty buffer!");
        }
        int amountOfBytesToBeSent = _byteBuffer.remaining();
        int bytesWritten = 0;
        try {
            bytesWritten = getChannel().write(_byteBuffer);
            if (bytesWritten != amountOfBytesToBeSent) {
                return false;
            }
            _byteBuffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _hasUnsendDataInBuffer = false;
        return true;
    }
