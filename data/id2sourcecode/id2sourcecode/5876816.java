    @Override
    public HttpResponse execute(int retries, RequestSetup setup) throws IOException {
        if (retries == 0) {
            return this.executeOnce(setup);
        } else {
            for (int i = 0; i <= retries; i++) {
                try {
                    return this.executeOnce(setup);
                } catch (IOException ex) {
                    if (i < retries && (ex instanceof SocketTimeoutException || ex.getMessage().startsWith("Timeout"))) {
                        log.warning("Timeout error, retrying");
                    } else {
                        throw ex;
                    }
                }
            }
            return null;
        }
    }
