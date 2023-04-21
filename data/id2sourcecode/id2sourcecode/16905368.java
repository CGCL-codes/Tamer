    public char[] loadSource(TemplateEngine engine) throws TemplateException {
        char[] source = null;
        try {
            CharArrayWriter writer = new CharArrayWriter();
            try {
                Reader reader = new InputStreamReader(getUrl().openStream(), getKey().getEncoding());
                try {
                    int length = 0;
                    char[] buffer = new char[4096];
                    while ((length = reader.read(buffer)) >= 0) writer.write(buffer, 0, length);
                } finally {
                    reader.close();
                }
                source = writer.toCharArray();
                updateMetadata();
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new TemplateLocatorException("Could not load template from \"" + this + "\"");
        }
        return source;
    }
