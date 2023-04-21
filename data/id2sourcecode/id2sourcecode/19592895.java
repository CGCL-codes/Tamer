    private byte[] getBytesFromResource(Object obj, SourceNode sourceNode) throws NamingException {
        byte[] bytes;
        if (obj instanceof org.apache.naming.resources.Resource) {
            org.apache.naming.resources.Resource resource = (org.apache.naming.resources.Resource) obj;
            bytes = resource.getContent();
            if (bytes == null) {
                try {
                    java.io.InputStream is = resource.streamContent();
                    int len;
                    while ((len = is.read(sourceNode.buff)) != -1) sourceNode.baos.write(sourceNode.buff, 0, len);
                    bytes = sourceNode.baos.toByteArray();
                    sourceNode.baos.reset();
                } catch (java.io.IOException ie) {
                    throw convertException("IO Error", ie);
                }
            }
        } else if (obj instanceof byte[]) bytes = (byte[]) obj; else throw new NamingException("Could not convert resource to byte array.");
        return bytes;
    }
