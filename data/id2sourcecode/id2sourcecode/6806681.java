    @Override
    public InputStream merge(final InputStream[] sources) {
        try {
            Assert.notNull(sources, "sources");
            Assert.notEmpty(sources, "sources");
            Document document = new Document();
            InputOutputStream outputStream = new InputOutputStream();
            PdfCopy writer = new PdfCopy(document, outputStream);
            document.open();
            for (InputStream source : sources) {
                PdfReader reader = new PdfReader(source);
                int pageCount = reader.getNumberOfPages();
                for (int i = 1; i <= pageCount; i++) {
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    writer.addPage(page);
                }
                reader.close();
            }
            document.close();
            writer.close();
            return outputStream.getInputStream();
        } catch (IOException e) {
            throw new PDFException(e);
        } catch (DocumentException e) {
            throw new PDFException(e);
        }
    }
