    public boolean executeExceptionMapperForClass(HttpRequest request, HttpResponse response, Throwable exception, Class clazz) {
        ExceptionMapper mapper = providerFactory.getExceptionMapper(clazz);
        if (mapper == null) return false;
        writeFailure(request, response, mapper.toResponse(exception));
        return true;
    }
