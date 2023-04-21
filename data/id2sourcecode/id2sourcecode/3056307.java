    public void store() throws SystemException {
        if (model == null || file == null) {
            throw new IllegalStateException("model and file must be set");
        }
        if (progressMonitor == null) {
            progressMonitor = new NullProgressMonitor();
        }
        final PipedOutputStream outStream = new PipedOutputStream();
        WriteThread writeThread = new WriteThread(model, outStream);
        try {
            PipedInputStream inStream = new PipedInputStream(outStream);
            writeThread.start();
            if (file.exists()) {
                file.setContents(inStream, IResource.FORCE, progressMonitor);
            } else {
                file.create(inStream, IResource.FORCE, progressMonitor);
            }
        } catch (Exception x) {
            throw new SystemException(x);
        }
        if (writeThread.error != null) {
            throw writeThread.error;
        }
    }
