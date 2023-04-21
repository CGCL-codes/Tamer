    public void close() throws IOException {
        try {
            super.commit();
            super.close("INPUT");
        } catch (SQLException e) {
            throw new IOException(e);
        }
        readAllowed = writeAllowed = rewriteAllowed = startAllowed = deleteAllowed = false;
        fileStatus = 0;
    }
