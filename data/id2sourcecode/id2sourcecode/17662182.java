    public XlsBook(URL url) throws IOException {
        InputStream is = url.openStream();
        workbook = XlsBook.createWorkbook(is);
        is.close();
    }
