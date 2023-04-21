    protected final byte[] content(final InputStream is) throws BXQException {
        valid(is, InputStream.class);
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final BufferedInputStream bis = new BufferedInputStream(is);
            int i = 0;
            while ((i = bis.read()) != -1) bos.write(i);
            bis.close();
            return bos.toByteArray();
        } catch (final IOException ex) {
            throw new BXQException(ex);
        }
    }
