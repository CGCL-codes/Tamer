    public static final Channel.ViewResponse getChannelViewResponse(final FutureTask<Channel.ViewResponse> channelViewTask) throws WWWeeePortal.Exception, WebApplicationException {
        if (channelViewTask == null) return null;
        try {
            channelViewTask.run();
            return channelViewTask.get();
        } catch (InterruptedException ie) {
            throw new WWWeeePortal.OperationalException(ie);
        } catch (ExecutionException ee) {
            final Throwable cause = ee.getCause();
            if (cause instanceof WebApplicationException) {
                throw (WebApplicationException) cause;
            } else if (cause instanceof WWWeeePortal.Exception) {
                throw (WWWeeePortal.Exception) cause;
            }
            throw new WWWeeePortal.SoftwareException(cause);
        }
    }
