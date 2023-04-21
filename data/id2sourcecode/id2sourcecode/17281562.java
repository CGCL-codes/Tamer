    protected void exceptionReceivedFromServer(Exception ex) throws Exception {
        serverException = ex;
        StackTraceElement[] serverTrace = ex.getStackTrace();
        StackTraceElement[] clientTrace = (new Throwable()).getStackTrace();
        StackTraceElement[] combinedTrace = new StackTraceElement[serverTrace.length + clientTrace.length];
        System.arraycopy(serverTrace, 0, combinedTrace, 0, serverTrace.length);
        System.arraycopy(clientTrace, 0, combinedTrace, serverTrace.length, clientTrace.length);
        ex.setStackTrace(combinedTrace);
        if (UnicastRef.clientCallLog.isLoggable(Log.BRIEF)) {
            TCPEndpoint ep = (TCPEndpoint) conn.getChannel().getEndpoint();
            UnicastRef.clientCallLog.log(Log.BRIEF, "outbound call " + "received exception: [" + ep.getHost() + ":" + ep.getPort() + "] exception: ", ex);
        }
        throw ex;
    }
