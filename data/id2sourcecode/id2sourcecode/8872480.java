    public int substitute(InputStream in, OutputStream out, String type, String encoding) throws IllegalArgumentException, UnsupportedEncodingException, IOException {
        if (encoding == null) {
            int t = getTypeConstant(type);
            switch(t) {
                case TYPE_JAVA_PROPERTIES:
                    encoding = "ISO-8859-1";
                    break;
                case TYPE_XML:
                    encoding = "UTF-8";
                    break;
            }
        }
        InputStreamReader reader = (encoding != null ? new InputStreamReader(in, encoding) : new InputStreamReader(in));
        OutputStreamWriter writer = (encoding != null ? new OutputStreamWriter(out, encoding) : new OutputStreamWriter(out));
        int subs = substitute(reader, writer, type);
        writer.flush();
        return subs;
    }
