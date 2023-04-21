    public void convertField(StringBuffer output, Tuple tuple, TupleMetadata tupleMetadata, int column) throws IOException, ColumnNotFoundException {
        InputStream stream = null;
        try {
            Blob value = tuple.getBlob(column);
            if (value == null) {
                output.append("<null/>");
                return;
            }
            stream = value.getBinaryStream();
        } catch (SQLException e) {
            IOException exception = new IOException("Could not read from BLOB");
            exception.initCause(e);
            throw exception;
        }
        if (stream != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] bytes = new byte[2048];
            int read = 0;
            while ((read = stream.read(bytes)) >= 0) {
                buffer.write(bytes, 0, read);
            }
            byte[] value = buffer.toByteArray();
            if ((value != null) && (value.length > 0)) {
                output.append(new String(Base64.encode(value)));
            }
        } else {
            output.append("<null/>");
        }
    }
