    @SuppressWarnings("PMD")
    public void testSerializeStackOverflow() throws Exception {
        int max = 500 * 1024 * 1024;
        int min = 0;
        boolean hasExpectedFailure = false;
        int middle = (max + min) / 2;
        while (!hasExpectedFailure && max > min) {
            try {
                if (smallStackTestSerializeStackOverflow(middle)) {
                    min = middle + 1;
                } else {
                    hasExpectedFailure = true;
                }
            } catch (Throwable throwable) {
                boolean stackOvf = false;
                Throwable current = throwable;
                while (!stackOvf && current != null) {
                    if (current instanceof StackOverflowError) {
                        max = middle;
                        stackOvf = true;
                    }
                    current = current.getCause();
                }
                if (!stackOvf) {
                    throw new Exception(throwable);
                }
                if (session.isOpened()) {
                    session.close(EnumFilePersistenceCloseAction.DO_NOT_SAVE);
                }
            }
            middle = (max + min) / 2;
        }
        assertTrue("has not expected failure", hasExpectedFailure);
        assertTrue("file persistence must be closed after error", filePersistence.isClosed());
        setUp();
        session.open();
        final BobSerialize bobSerialize = (BobSerialize) session.getObject(BIG_BOB_KEY);
        assertNull(bobSerialize);
        session.closeAndWait(EnumFilePersistenceCloseAction.SAVE);
    }
