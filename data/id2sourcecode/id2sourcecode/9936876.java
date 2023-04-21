    public static void concatFiles(InputStream[] streamin, OutputStream target) {
        try {
            Document document = null;
            PdfCopy writer = null;
            for (int i = 0; i < streamin.length; i++) {
                PdfReader reader = new PdfReader(streamin[i]);
                reader.consolidateNamedDestinations();
                int numberofpages = reader.getNumberOfPages();
                if (i == 0) {
                    document = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, target);
                    document.open();
                }
                PdfImportedPage page;
                for (int x = 1; x <= numberofpages; x++) {
                    page = writer.getImportedPage(reader, x);
                    writer.addPage(page);
                }
                writer.freeReader(reader);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
