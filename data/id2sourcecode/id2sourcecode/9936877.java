    public static void concatFiles(String path, String target) {
        String[] files = new File(path).list(new PDFFilter());
        try {
            Document document = null;
            PdfCopy writer = null;
            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(path + files[i]);
                reader.consolidateNamedDestinations();
                int numberofpages = reader.getNumberOfPages();
                if (log.isDebugEnabled()) log.debug("There are " + numberofpages + " pages in " + files[i]);
                if (i == 0) {
                    document = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, new FileOutputStream(target));
                    document.open();
                }
                PdfImportedPage page;
                for (int x = 1; x <= numberofpages; x++) {
                    page = writer.getImportedPage(reader, x);
                    writer.addPage(page);
                    log.debug("Processed page " + x);
                }
                writer.freeReader(reader);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
