    @Override
    public Annotation[] getAnnotations() {
        return reader == null ? writer.getAnnotations() : reader.getAnnotations();
    }
