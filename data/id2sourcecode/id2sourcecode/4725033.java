    void writeAllPages() throws IOException {
        try {
            file.reOpen();
            for (Iterator it = importedPages.values().iterator(); it.hasNext(); ) {
                PdfImportedPage ip = (PdfImportedPage) it.next();
                writer.addToBody(ip.getFormXObject(writer.getCompressionLevel()), ip.getIndirectReference());
            }
            writeAllVisited();
        } finally {
            try {
                reader.close();
                file.close();
            } catch (Exception e) {
            }
        }
    }
