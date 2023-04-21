    public String process(Reader reader, Writer writer) throws IOException {
        String requestString = IOUtils.toString(reader);
        if (logger.isDebugEnabled()) {
            logger.debug("Request data (JSON)=>" + requestString);
        }
        JsonRequestData[] requests = getIndividualJsonRequests(requestString);
        final boolean isBatched = requests.length > 1;
        if (isBatched) {
            if (logger.isDebugEnabled()) {
                logger.debug("Batched request: " + requests.length + " individual requests batched");
            }
        }
        Collection<ResponseData> responses = null;
        boolean useMultipleThreadsIfBatched = isBatched && getGlobalConfiguration().getBatchRequestsMultithreadingEnabled();
        if (useMultipleThreadsIfBatched) {
            responses = processIndividualRequestsInMultipleThreads(requests);
        } else {
            responses = processIndividualRequestsInThisThread(requests);
        }
        String result = convertInvididualResponsesToJsonString(responses);
        writer.write(result);
        if (logger.isDebugEnabled()) {
            logger.debug("ResponseData data (JSON)=>" + result);
        }
        return result;
    }
