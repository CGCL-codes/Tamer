    void writeAllPages() throws IOException {
        try {
            file.reOpen();
            for (Object element : importedPages.values()) {
                PdfImportedPage ip = (PdfImportedPage) element;
                if (ip.isToCopy()) {
                    writer.addToBody(ip.getFormXObject(writer.getCompressionLevel()), ip.getIndirectReference());
                    ip.setCopied();
                }
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
