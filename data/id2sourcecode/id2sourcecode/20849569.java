    public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext ctx) {
        Blob blob = (Blob) obj;
        InputStream in = null;
        ByteArrayOutputStream bout = null;
        try {
            in = blob.getBinaryStream();
            bout = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readBytes = in.read(buffer);
            while (readBytes > 0) {
                bout.write(buffer, 0, readBytes);
                readBytes = in.read(buffer);
            }
            writer.setValue(Compresser.compressToBase64Str(bout.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
