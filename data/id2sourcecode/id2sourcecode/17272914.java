    public boolean writeToNetwork() {
        int amountOfBytesToBeSent = _byteBuffer.position();
        _byteBuffer.flip();
        int bytesWritten;
        try {
            bytesWritten = getChannel().write(_byteBuffer);
            if (bytesWritten != amountOfBytesToBeSent) {
                _hasUnsendDataInBuffer = true;
                return false;
            }
            _byteBuffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
