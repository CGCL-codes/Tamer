    public String substitute(InputStream in, String type) throws IllegalArgumentException, UnsupportedEncodingException, IOException {
        String encoding = PLAIN;
        {
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
        InputStreamReader reader = ((encoding != null) ? new InputStreamReader(in, encoding) : new InputStreamReader(in));
        StringWriter writer = new StringWriter();
        substitute(reader, writer, type);
        writer.flush();
        return writer.getBuffer().toString();
    }
