    public static void main(String[] args) {
        try {
            PdfImportedPage page;
            PdfDictionary pageDict;
            PdfReader reader = new PdfReader(RESOURCE);
            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT2));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                page = writer.getImportedPage(reader, 1);
                cb.rectangle(0, 52, 612, 668);
                cb.clip();
                cb.newPath();
                cb.addTemplate(page, 0, 0);
                pageDict = reader.getPageN(i);
                PdfArray crop = new PdfRectangle(0, 52, 612, 720);
                pageDict.put(PdfName.CROPBOX, crop);
            }
            document.close();
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT1));
            stamper.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
