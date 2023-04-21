    public int write(byte[] buffer, int offset, int length) throws IPCException {
        if (getDirection() != PipeDirection.Writer) {
            throw new IPCException("Attempt to write to a read-only pipe.");
        }
        NamedPipeResult result = new NamedPipeResult();
        writeImpl(result, buffer, offset, length);
        if (result.resultCode != NamedPipeResult.SUCCESS) {
            String msg = "Error writing named pipe, error code = " + result.errorCode;
            throw new IPCException(msg);
        }
        return result.byteCount;
    }
