    protected void doWriteReturnInputStream(final StreamSupportingRemoteInvocationResult result, final OutputStream unbufferedChunkedOut) throws IOException {
        final InputStream isResult = result.getServerSideInputStream();
        if (isResult != null) {
            try {
                final byte[] buffer = new byte[4096];
                int read;
                while ((read = isResult.read(buffer)) != -1) {
                    unbufferedChunkedOut.write(buffer, 0, read);
                }
            } finally {
                result.setServerSideInputStream(null);
                isResult.close();
            }
        }
    }
