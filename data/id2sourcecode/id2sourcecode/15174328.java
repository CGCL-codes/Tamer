    public Object nullSafeGet(ResultSet arg0, String[] arg1, Object arg2) throws HibernateException, SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        InputStream is = null;
        try {
            is = arg0.getBinaryStream(arg1[0]);
            if (arg0.wasNull()) return "";
            byte[] buf = new byte[1024];
            int read = -1;
            while ((read = is.read(buf)) > 0) {
                baos.write(buf, 0, read);
            }
            String ret = baos.toString(CHAR_SET);
            return ret;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new HibernateException("Unable to read from resultset", ioe);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
