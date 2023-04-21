    public Object setFieldValue(ResultSet rs, Object entity, Field f, int index) throws Exception {
        Reader reader = rs.getCharacterStream(index);
        if (reader != null) {
            StringWriter writer = new StringWriter();
            char[] buf = new char[2048];
            for (int len = -1; (len = reader.read(buf, 0, 2048)) != -1; ) {
                writer.write(buf, 0, len);
            }
            reader = new StringReader(writer.toString());
        }
        boolean isAccess = f.isAccessible();
        f.setAccessible(true);
        f.set(entity, reader);
        f.setAccessible(isAccess);
        return reader;
    }
