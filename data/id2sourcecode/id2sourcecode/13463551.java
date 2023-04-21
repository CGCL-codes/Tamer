    private void compileAnnotations() throws SecurityException, NoSuchMethodException {
        Method method = this.businessClass.getMethod("execute", HttpRequest.class, HttpResponse.class);
        NotNull notNull = method.getAnnotation(NotNull.class);
        if (notNull != null) this.validatorList.add(new NotNullValidator(notNull));
        NotOutOfBound notOutOfBound = method.getAnnotation(NotOutOfBound.class);
        if (notOutOfBound != null) this.validatorList.add(new NotOutOfBoundValidator(notOutOfBound));
    }
