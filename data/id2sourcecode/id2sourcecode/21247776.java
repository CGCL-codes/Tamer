    private void assertWritable() throws ReplicatorException {
        assertNotDone();
        if (readonly) {
            throw new THLException("Attempt to write using read-only log connection");
        }
        if (!diskLog.isWritable()) {
            throw new THLException("Attempt to write using read-only log connection");
        }
    }
