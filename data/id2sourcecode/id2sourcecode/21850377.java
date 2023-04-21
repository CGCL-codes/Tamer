    @Override
    public byte[] getHash(final InputStream is, final int bufferSize) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(is, bufferSize));
        if (null == is) {
            throw new RuntimeExceptionIsNull("is");
        }
        if (1 > bufferSize) {
            throw new RuntimeExceptionMustBeGreater("bufferSize", bufferSize, 1);
        }
        if (bufferSize > HelperEnvironment.getMemoryFree()) {
            throw new RuntimeExceptionExceedsVmMemory("bufferSize", bufferSize);
        }
        md.reset();
        final byte[] buffer = new byte[bufferSize];
        int offset = is.read(buffer);
        while (0 < offset) {
            md.update(buffer, 0, offset);
            offset = is.read(buffer);
        }
        final byte[] result = md.digest();
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(result));
        return result;
    }
