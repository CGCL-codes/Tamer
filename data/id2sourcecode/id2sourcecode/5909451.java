    protected void doUnexpectedFailure(Throwable e) {
        try {
            getThreadLocalResponse().reset();
        } catch (IllegalStateException ex) {
            throw new RuntimeException("Unable to report failure", e);
        }
        ServletContext servletContext = getServletContext();
        RPCServletUtils.writeResponseForUnexpectedFailure(servletContext, getThreadLocalResponse(), e);
    }
