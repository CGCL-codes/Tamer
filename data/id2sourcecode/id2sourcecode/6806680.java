    @Override
    public InputStream cut(final InputStream source, final int start, final int end) {
        try {
            Assert.notNull(source, "source");
            Assert.greaterOrEqual(start, 1, "start");
            PdfReader reader = new PdfReader(source);
            Document document = new Document(reader.getPageSizeWithRotation(1));
            InputOutputStream outputStream = new InputOutputStream();
            PdfCopy writer = new PdfCopy(document, outputStream);
            int pageCount = reader.getNumberOfPages();
            Assert.lessOrEqual(start, pageCount, "start");
            int endPage = end;
            if (endPage > pageCount) {
                endPage = pageCount;
            }
            document.open();
            for (int i = start; i <= endPage; i++) {
                PdfImportedPage page = writer.getImportedPage(reader, i);
                writer.addPage(page);
            }
            document.close();
            writer.close();
            reader.close();
            return outputStream.getInputStream();
        } catch (IOException e) {
            throw new PDFException(e);
        } catch (DocumentException e) {
            throw new PDFException(e);
        }
    }
