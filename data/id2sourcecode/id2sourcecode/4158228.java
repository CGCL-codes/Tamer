    private void createReader(String name, String[] pageContents) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        for (int i = 0; i < pageContents.length; i++) {
            if (i != 0) document.newPage();
            String content = pageContents[i];
            Chunk contentChunk = new Chunk(content);
            document.add(contentChunk);
        }
        document.close();
        pdfContent.put(name, baos.toByteArray());
    }
