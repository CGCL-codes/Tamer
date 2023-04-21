    public void setContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
        try {
            logger.log(Level.FINEST, "Hi!");
            OutputStream os = getStore().openOutputStream(0, null);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = source.read(buffer);
                if (read != 0) os.write(buffer, 0, read); else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CoreException(Activator.createErrorStatus(e));
        }
    }
