    public Object nullSafeGet(ResultSet arg0, String[] arg1, Object arg2) throws HibernateException, SQLException {
        String ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        try {
            InputStream is = arg0.getBinaryStream(arg1[0]);
            byte[] buf = new byte[1024];
            int read = -1;
            while ((read = is.read(buf)) > 0) {
                baos.write(buf, 0, read);
            }
            is.close();
            ret = baos.toString(CHAR_SET);
            baos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new HibernateException("Unable to read from resultset", ioe);
        }
        return ret;
    }
