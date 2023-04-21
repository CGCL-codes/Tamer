    public void setValue(PreparedStatement ps, Object value, int index) throws Exception {
        InputStream ips = (InputStream) value;
        if (ips == null) ps.setNull(index, java.sql.Types.LONGVARBINARY);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        byte[] b = new byte[4096];
        for (int len = -1; (len = ips.read(b, 0, 4096)) != -1; ) baos.write(b, 0, len);
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(baos.toByteArray());
        ps.setBinaryStream(index, bais, baos.size());
    }
