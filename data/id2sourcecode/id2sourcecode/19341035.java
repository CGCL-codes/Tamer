    public void process(Reader reader, Writer writer, String pathInfo) throws IOException {
        assert !StringUtils.isEmpty(pathInfo);
        this.requestString = IOUtils.toString(reader);
        this.eventName = getEventName(pathInfo);
        logEnterInfo();
        ResponseData response;
        try {
            RegisteredMethod method = getMethod();
            Object[] parameters = getParameters();
            Object result = getDispatcher().dispatch(method, parameters);
            response = createSuccessResponse(result);
        } catch (Throwable t) {
            Throwable reportedException = RequestProcessorUtils.getExceptionToReport(t);
            String message = RequestProcessorUtils.getExceptionMessage(reportedException);
            String where = RequestProcessorUtils.getExceptionWhere(reportedException, getDebug());
            response = createErrorResponse(message, where);
            logErrorResponse(t);
        }
        StringBuilder result = new StringBuilder();
        appendIndividualResponseJsonString(response, result);
        this.resultString = result.toString();
        writer.write(this.resultString);
        logExitInfo();
    }
