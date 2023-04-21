    @Override
    public void writeTo(final Track t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        if (mediaType.isCompatible(MediaType.APPLICATION_OCTET_STREAM_TYPE)) {
            final InputStream in = new FileInputStream(new File(t.getPath()));
            int c;
            while ((c = in.read()) != -1) entityStream.write(c);
        }
    }
