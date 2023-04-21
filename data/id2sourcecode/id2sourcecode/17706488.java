    public int read(byte[] buffer, int offset, int length) throws IPCException {
        if (PipeDirection.Reader != getDirection()) {
            throw new IPCException("Attempt to read a write-only pipe.");
        }
        NamedPipeResult result = new NamedPipeResult();
        readImpl(result, buffer, offset, length);
        if (result.resultCode != NamedPipeResult.SUCCESS) {
            String msg = "Error reading named pipe, code = " + result.errorCode;
            throw new IPCException(msg);
        }
        return result.byteCount;
    }
