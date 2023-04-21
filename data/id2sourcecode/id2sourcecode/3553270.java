    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        InputStream blobReader = rs.getBinaryStream(names[0]);
        if (blobReader == null) return null;
        byte[] b = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            while ((blobReader.read(b)) != -1) os.write(b);
        } catch (IOException e) {
            throw new SQLException(e.toString());
        } finally {
            try {
                os.close();
            } catch (IOException e) {
            }
        }
        return new String((String) CaBIG_LobUtilities.decodeLob(os.toByteArray()));
    }
