    public boolean executeExactExceptionMapper(HttpRequest request, HttpResponse response, Throwable exception) {
        ExceptionMapper mapper = providerFactory.getExceptionMapper(exception.getClass());
        if (mapper == null) return false;
        writeFailure(request, response, mapper.toResponse(exception));
        return true;
    }
