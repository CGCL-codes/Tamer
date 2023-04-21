    public void copyStream(InputStream inputStream) throws SQLException {
        OutputStream os = setBinaryStream(1);
        try {
            int chunk = 0;
            byte[] buffer = new byte[bufferlength];
            while ((chunk = inputStream.read(buffer)) != -1) os.write(buffer, 0, chunk);
            os.flush();
            os.close();
        } catch (IOException ioe) {
            throw new FBSQLException(ioe);
        }
    }
