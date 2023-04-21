    private static Result toResult(Object xml) throws IOException {
        if (xml == null) throw new IllegalArgumentException("no XML is given");
        if (xml instanceof String) {
            try {
                xml = new URI((String) xml);
            } catch (URISyntaxException e) {
                xml = new File((String) xml);
            }
        }
        if (xml instanceof File) {
            File file = (File) xml;
            return new StreamResult(file);
        }
        if (xml instanceof URI) {
            URI uri = (URI) xml;
            xml = uri.toURL();
        }
        if (xml instanceof URL) {
            URL url = (URL) xml;
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(false);
            con.connect();
            return new StreamResult(con.getOutputStream());
        }
        if (xml instanceof OutputStream) {
            OutputStream os = (OutputStream) xml;
            return new StreamResult(os);
        }
        if (xml instanceof Writer) {
            Writer w = (Writer) xml;
            return new StreamResult(w);
        }
        if (xml instanceof Result) {
            return (Result) xml;
        }
        throw new IllegalArgumentException("I don't understand how to handle " + xml.getClass());
    }
