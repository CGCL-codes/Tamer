    @Override
    public void writeTo(Reader reader, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        final CharacterSet cs = Response.getCurrent().getEntity().getCharacterSet();
        Util.copyStream(BioUtils.getStream(reader, cs), entityStream);
    }
