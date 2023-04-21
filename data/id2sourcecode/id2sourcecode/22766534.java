    protected Blob createTemporaryBlob(InputStream in) throws Exception {
        Method createTemporary = blobClass.getMethod("createTemporary", new Class[] { Connection.class, Boolean.TYPE, Integer.TYPE });
        Object blob = createTemporary.invoke(null, new Object[] { con, Boolean.FALSE, DURATION_SESSION_CONSTANT });
        Method open = blobClass.getMethod("open", new Class[] { Integer.TYPE });
        open.invoke(blob, new Object[] { MODE_READWRITE_CONSTANT });
        Method getBinaryOutputStream = blobClass.getMethod("getBinaryOutputStream", new Class[0]);
        OutputStream out = (OutputStream) getBinaryOutputStream.invoke(blob, null);
        try {
            int read;
            byte[] buf = new byte[8192];
            while ((read = in.read(buf, 0, buf.length)) > -1) {
                out.write(buf, 0, read);
            }
        } finally {
            try {
                out.flush();
            } catch (IOException ioe) {
                LOG.error(null, ioe);
            }
            out.close();
        }
        Method close = blobClass.getMethod("close", new Class[0]);
        close.invoke(blob, null);
        return (Blob) blob;
    }
