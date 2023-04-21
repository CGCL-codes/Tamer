    public void setCharacterStream(Reader in, int length) throws SQLException {
        if (in == READER_NULL_VALUE) {
            setNull();
            return;
        }
        try {
            StringWriter out = new StringWriter();
            char[] buff = new char[4096];
            int counter = 0;
            while ((counter = in.read(buff)) != -1) out.write(buff, 0, counter);
            String outString = out.toString();
            setString(outString.substring(0, length));
        } catch (IOException ioex) {
            throw (SQLException) createException(CHARACTER_STREAM_CONVERSION_ERROR).fillInStackTrace();
        }
    }
