    public Object setFieldValue(ResultSet rs, Object entity, Method m, int index) throws Exception {
        InputStream ins = rs.getBinaryStream(index);
        if (ins != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            for (int len = -1; (len = ins.read(b, 0, 2048)) != -1; ) baos.write(b, 0, len);
            ins = new java.io.ByteArrayInputStream(baos.toByteArray());
        }
        m.invoke(entity, new Object[] { ins });
        return ins;
    }
