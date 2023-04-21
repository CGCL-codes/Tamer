    public JavaSource getJavaSource(final JavaSourceFactory pFactory) throws RecognitionException, TokenStreamException, IOException {
        List result;
        if (file == null) {
            InputStream istream = null;
            try {
                istream = url.openStream();
                Reader r = new InputStreamReader(istream);
                result = new JavaParser(pFactory).parse(r);
                istream.close();
                istream = null;
            } finally {
                if (istream != null) {
                    try {
                        istream.close();
                    } catch (Throwable ignore) {
                    }
                }
            }
        } else {
            Reader r = null;
            try {
                r = new FileReader(file);
                result = new JavaParser(pFactory).parse(r);
                r.close();
                r = null;
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Throwable ignore) {
                    }
                }
            }
        }
        if (result.size() > 1) {
            throw new RecognitionException("The Java source file contained multiple classes.");
        }
        if (result.size() > 1) {
            throw new RecognitionException("The Java source file contained multiple classes.");
        }
        return (JavaSource) result.get(0);
    }
