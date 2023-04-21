    void parseFile(InputStream in, String file, boolean ignoreCharset) throws IOException {
        try {
            kit.parse(new InputStreamReader(in, charSetName), file, ignoreCharset, indexBuilder, config);
        } catch (com.sun.java.help.search.ChangedCharSetException e1) {
            String charSetSpec = e1.getCharSetSpec();
            if (e1.keyEqualsCharSet()) {
                charSetName = charSetSpec;
            } else {
                setCharsetFromContentTypeParameters(charSetSpec);
            }
            in.close();
            URL url = new URL("file", "", sourcepath + file);
            in = url.openStream();
            parseFile(in, file, true);
        }
    }
