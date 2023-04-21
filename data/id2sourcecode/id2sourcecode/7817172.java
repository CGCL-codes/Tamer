    public Object getContent() throws java.io.IOException {
        try {
            return getDataHandler().getContent();
        } catch (UnsupportedEncodingException uee) {
            if (isText()) {
                InputStream is = getDataHandler().getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int b;
                while ((b = is.read()) != -1) bos.write(b);
                byte[] barray = bos.toByteArray();
                return new String(barray, Pooka.getProperty("Pooka.defaultCharset", "iso-8859-1"));
            } else {
                throw uee;
            }
        }
    }
