    public void writePdf() throws FileNotFoundException, DocumentException, IOException {
        targetFile = new File(getTarget());
        PdfWriter.getInstance(this, new FileOutputStream(targetFile));
        this.open();
        weekMenuParagraph.add(weekTable);
        this.add(weekMenuParagraph);
        shopListParagraph.add(shopTable);
        this.add(shopListParagraph);
        this.close();
    }
