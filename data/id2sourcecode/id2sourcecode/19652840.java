    public InputStream getInputStream(IProgressMonitor monitor) throws IOException, CoreException, TooManyOpenConnectionsException {
        if (in == null && url != null) {
            if (connection == null || offset > 0) connection = url.openConnection();
            if (offset > 0) connection.setRequestProperty("Range", "bytes=" + offset + "-");
            if (monitor != null) {
                try {
                    this.in = new MonitoringInputStream(openStreamWithCancel(connection, monitor), connection);
                } catch (IOException ioe) {
                    connection = null;
                    throw ioe;
                }
            } else {
                try {
                    this.in = new MonitoringInputStream(connection.getInputStream(), connection);
                } catch (IOException ioe) {
                    connection = null;
                    throw ioe;
                }
            }
            checkOffset();
            if (connection != null) {
                this.lastModified = connection.getLastModified();
            }
        }
        return in;
    }
