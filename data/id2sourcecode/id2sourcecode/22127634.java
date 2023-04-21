    public void convertField(StringBuffer output, ResultSet rs, int column) throws SQLException, IOException {
        InputStream stream = rs.getBinaryStream(column);
        if (stream != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] bytes = new byte[2048];
            int read = 0;
            while ((read = stream.read(bytes)) >= 0) {
                buffer.write(bytes, 0, read);
            }
            byte[] value = buffer.toByteArray();
            if ((value != null) && (value.length > 0)) {
                output.append(Base64.encode(value));
            }
        } else {
            output.append("<null/>");
        }
    }
