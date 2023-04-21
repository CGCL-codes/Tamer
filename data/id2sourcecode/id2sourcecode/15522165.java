    public int write(byte[] buffer, int offset, int length, int timeoutMsec) throws IPCException {
        if (getDirection() != PipeDirection.Writer) {
            throw new IPCException("Attempt to write to a read-only pipe.");
        }
        FIFOResult result = new FIFOResult();
        writeImpl(result, buffer, offset, length, timeoutMsec);
        if (result.resultCode != FIFOResult.SUCCESS) {
            String msg = "Error writing named pipe, error code = " + result.errorCode;
            throw new IPCException(msg);
        }
        return result.byteCount;
    }
