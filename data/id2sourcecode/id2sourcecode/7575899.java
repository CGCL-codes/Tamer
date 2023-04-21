    public Object setFieldValue(Object entity, Field f, Object value) throws Exception {
        boolean isAccess = f.isAccessible();
        InputStream ins = (InputStream) value;
        if (ins != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            for (int len = -1; (len = ins.read(b, 0, 2048)) != -1; ) baos.write(b, 0, len);
            ins = new java.io.ByteArrayInputStream(baos.toByteArray());
        }
        f.setAccessible(true);
        f.set(entity, ins);
        f.setAccessible(isAccess);
        return ins;
    }
