    public static void extractPages(OutputStream pdfOut, InputStream pdfIn, int[] pages) throws Exception {
        if (pages.length <= 0) {
            throw new Exception("Debe eliminar al menos una p�gina");
        }
        List pagesToKeep = new ArrayList();
        for (int i = 0; i < pages.length; i++) {
            pagesToKeep.add(new Integer(pages[i]));
        }
        PdfCopyFields writer = new PdfCopyFields(pdfOut);
        int permission = 0;
        PdfReader reader = new PdfReader(pdfIn);
        permission = reader.getPermissions();
        writer.setEncryption(null, null, permission, PdfWriter.STRENGTH40BITS);
        writer.addDocument(reader, pagesToKeep);
        writer.close();
    }
