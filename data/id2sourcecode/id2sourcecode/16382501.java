    <T> T call() throws IOException {
        if (shouldMock()) {
            return this.<T>createMockResultOfCall();
        }
        final long start = System.currentTimeMillis();
        try {
            final URLConnection connection = openConnection(url, headers);
            connection.setRequestProperty("Accept-Language", I18N.getCurrentLocale().getLanguage());
            connection.connect();
            @SuppressWarnings("unchecked") final T result = (T) read(connection);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("read on " + url + " : " + result);
            }
            if (result instanceof RuntimeException) {
                throw (RuntimeException) result;
            } else if (result instanceof Error) {
                throw (Error) result;
            } else if (result instanceof IOException) {
                throw (IOException) result;
            } else if (result instanceof Exception) {
                throw createIOException((Exception) result);
            }
            return result;
        } catch (final ClassNotFoundException e) {
            throw createIOException(e);
        } finally {
            LOGGER.info("http call done in " + (System.currentTimeMillis() - start) + " ms for " + url);
        }
    }
