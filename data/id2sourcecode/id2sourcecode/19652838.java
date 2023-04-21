    public InputStream getInputStream() throws IOException {
        if (in == null && url != null) {
            if (connection == null || offset > 0) connection = url.openConnection();
            if (offset > 0) connection.setRequestProperty("Range", "bytes=" + offset + "-");
            try {
                in = new MonitoringInputStream(connection.getInputStream(), connection);
            } catch (IOException ioe) {
                connection = null;
                throw ioe;
            }
            checkOffset();
        }
        return in;
    }
