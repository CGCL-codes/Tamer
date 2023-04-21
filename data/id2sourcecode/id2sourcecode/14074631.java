    public void put(OutputStream destination) throws IOException {
        retries++;
        byte[] data = new byte[BrowsableFileSystem.DEFAULT_BUFFER_SIZE];
        int read = 0;
        long totalRead = 0;
        float stackedWork = 0;
        boolean cancelled = false;
        while (((read = source.read(data)) > 0) && !cancelled) {
            destination.write(data, 0, read);
            destination.flush();
            totalRead += read;
            if (monitor != null) {
                stackedWork += (read * incrementSize);
                if (stackedWork >= 1) {
                    monitor.worked((int) stackedWork);
                    stackedWork = stackedWork - ((int) stackedWork);
                }
                if (monitor.isCanceled()) {
                    cancelled = true;
                }
            }
        }
        setSize(totalRead);
        source.close();
        destination.close();
    }
