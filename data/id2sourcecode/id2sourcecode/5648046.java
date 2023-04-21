    public void write(Writer writer, int columns) throws IOException {
        String xml = document.asXML();
        String base64 = new String(Base64.encodeBase64(xml.getBytes("utf-8")));
        if (columns > 0) base64 = addLineBreaks(base64, columns);
        StringReader reader = new StringReader(base64);
        char buffer[] = new char[32768];
        int len;
        while ((len = reader.read(buffer)) != -1) writer.write(buffer, 0, len);
    }
