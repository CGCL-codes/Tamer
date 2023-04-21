    public static void concatenarPdf(OutputStream pdfOut, InputStream[] pdfIn) throws Exception {
        if (pdfIn.length < 2) {
            throw new Exception("Debe concatenar al menos 2 PDFs");
        }
        int f = 0;
        PdfCopyFields writer = new PdfCopyFields(pdfOut);
        int permission = 0;
        while (f < pdfIn.length) {
            PdfReader reader = new PdfReader(pdfIn[f]);
            if (f == 0) {
                permission = reader.getPermissions();
                writer.setEncryption(null, null, permission, PdfWriter.STRENGTH40BITS);
            }
            writer.addDocument(reader);
            f++;
        }
        writer.close();
    }
