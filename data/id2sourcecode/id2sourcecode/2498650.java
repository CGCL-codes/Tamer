    @Override
    protected void doUnexpectedFailure(Throwable e) {
        ServletContext servletContext = getServletContext();
        if (e instanceof UnexpectedException) e = ((UnexpectedException) e).getCause();
        writeResponseForUnexpectedFailure(servletContext, getThreadLocalResponse(), e);
    }
