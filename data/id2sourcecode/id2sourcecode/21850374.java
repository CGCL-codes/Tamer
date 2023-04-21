    @Override
    public byte[] getHash(final byte... input) {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(input));
        if (null == input) {
            throw new RuntimeExceptionIsNull("input");
        }
        if (!HelperArray.isValid(input)) {
            throw new RuntimeExceptionIsEmpty("input");
        }
        md.reset();
        md.update(input);
        final byte[] result = md.digest();
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(result));
        return result;
    }
