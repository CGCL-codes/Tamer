    @Override
    public InputStream[] split(final InputStream source, final int size) {
        try {
            Assert.notNull(source, "source");
            Assert.greaterThan(size, 0, "size");
            PdfReader reader = new PdfReader(source);
            int pageCount = reader.getNumberOfPages();
            List<InputStream> list = new LinkedList<InputStream>();
            Document document = null;
            InputOutputStream outputStream = null;
            PdfCopy writer = null;
            for (int i = 1; i <= pageCount; i++) {
                if ((document == null) || ((i % size) == 0)) {
                    if (document != null) {
                        document.close();
                        writer.close();
                        list.add(outputStream.getInputStream());
                    }
                    document = new Document(reader.getPageSizeWithRotation(1));
                    outputStream = new InputOutputStream();
                    writer = new PdfCopy(document, outputStream);
                }
                PdfImportedPage page = writer.getImportedPage(reader, i);
                writer.addPage(page);
            }
            if (document != null) {
                document.close();
                writer.close();
                list.add(outputStream.getInputStream());
            }
            reader.close();
            return list.toArray(new InputStream[list.size()]);
        } catch (IOException e) {
            throw new PDFException(e);
        } catch (DocumentException e) {
            throw new PDFException(e);
        }
    }
