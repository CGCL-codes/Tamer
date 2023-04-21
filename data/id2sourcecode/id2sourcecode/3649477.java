    public void manipulatePdf(String[] src, String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest));
        document.open();
        PdfReader reader;
        int page_offset = 0;
        int n;
        ArrayList<HashMap<String, Object>> bookmarks = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> tmp;
        for (int i = 0; i < src.length; i++) {
            reader = new PdfReader(src[i]);
            tmp = SimpleBookmark.getBookmark(reader);
            SimpleBookmark.shiftPageNumbers(tmp, page_offset, null);
            bookmarks.addAll(tmp);
            n = reader.getNumberOfPages();
            page_offset += n;
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(reader, ++page));
            }
            copy.freeReader(reader);
        }
        copy.setOutlines(bookmarks);
        document.close();
    }
