    public static void mergePDFs(List<String> list) throws FileNotFoundException, DocumentException, IOException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(fullPath + "master.pdf"));
        document.open();
        PdfReader reader;
        int n;
        for (int i = 0; i < list.size(); i++) {
            reader = new PdfReader(list.get(i), "Gu7ruc*YAWaStEbr".getBytes());
            n = reader.getNumberOfPages();
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(reader, ++page));
            }
            copy.freeReader(reader);
        }
        document.close();
        copy.close();
    }
