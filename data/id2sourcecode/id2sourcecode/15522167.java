    public int read(byte[] buffer, int offset, int length, int timeoutMsec) throws IPCException {
        if (PipeDirection.Reader != getDirection()) {
            throw new IPCException("Attempt to read a write-only pipe.");
        }
        FIFOResult result = new FIFOResult();
        readImpl(result, buffer, offset, length, timeoutMsec);
        if (result.resultCode == FIFOResult.ERROR_PIPE_CLOSED) {
            return -1;
        }
        if (result.resultCode != FIFOResult.SUCCESS) {
            String msg = "Error reading named pipe, code = " + result.errorCode;
            throw new IPCException(msg);
        }
        return result.byteCount;
    }
