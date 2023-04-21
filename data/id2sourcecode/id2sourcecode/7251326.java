    public boolean executeExceptionMapper(HttpRequest request, HttpResponse response, Throwable exception) {
        ExceptionMapper mapper = null;
        Class causeClass = exception.getClass();
        while (mapper == null) {
            if (causeClass == null) break;
            mapper = providerFactory.getExceptionMapper(causeClass);
            if (mapper == null) causeClass = causeClass.getSuperclass();
        }
        if (mapper != null) {
            writeFailure(request, response, mapper.toResponse(exception));
            return true;
        }
        return false;
    }
