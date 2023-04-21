    protected InputStream openStreamWithCancel(URLConnection urlConnection, IProgressMonitor monitor) throws IOException, CoreException, TooManyOpenConnectionsException {
        ConnectionThreadManager.StreamRunnable runnable = new ConnectionThreadManager.StreamRunnable(urlConnection);
        Thread t = ConnectionThreadManagerFactory.getConnectionManager().getConnectionThread(runnable);
        t.start();
        InputStream is = null;
        try {
            for (; ; ) {
                if (monitor.isCanceled()) {
                    runnable.disconnect();
                    connection = null;
                    break;
                }
                if (runnable.getInputStream() != null || !t.isAlive()) {
                    is = runnable.getInputStream();
                    break;
                }
                if (runnable.getIOException() != null) throw runnable.getIOException();
                if (runnable.getException() != null) throw new CoreException(new Status(IStatus.ERROR, UpdateCore.getPlugin().getBundle().getSymbolicName(), IStatus.OK, runnable.getException().getMessage(), runnable.getException()));
                t.join(POLLING_INTERVAL);
            }
        } catch (InterruptedException e) {
        }
        return is;
    }
