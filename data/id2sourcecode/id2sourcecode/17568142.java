    public void copyCharacterStream(Reader characterStream) throws SQLException {
        Writer writer = setCharacterStream(0);
        try {
            int chunk = 0;
            char[] buffer = new char[1024];
            while ((chunk = characterStream.read(buffer)) != -1) writer.write(buffer, 0, chunk);
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            throw new FBSQLException(ioe);
        }
    }
