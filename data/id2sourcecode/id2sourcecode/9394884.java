    static Document createDictionary0(URL url) throws IOException {
        Document dictDoc = null;
        InputStream in = null;
        try {
            in = url.openStream();
            dictDoc = new CMLBuilder().build(in);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new RuntimeException("NULL " + e.getMessage() + S_SLASH + e.getCause() + " in " + url);
        } catch (ValidityException e) {
            throw new RuntimeException(S_EMPTY + e.getMessage() + S_SLASH + e.getCause() + " in " + url);
        } catch (ParsingException e) {
            System.err.println("ERR at line/col " + e.getLineNumber() + S_SLASH + e.getColumnNumber());
            throw new RuntimeException(" in " + url, e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dictDoc;
    }
