    public static void main(String[] args) {
        createPdf(SOURCE);
        Document document = new Document(PageSize.A4);
        try {
            PdfReader reader = new PdfReader(SOURCE);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            PdfImportedPage page;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                page = writer.getImportedPage(reader, i);
                Image image = Image.getInstance(page);
                image.scalePercent(15f);
                image.setBorder(Rectangle.BOX);
                image.setBorderWidth(3f);
                image.setBorderColor(new GrayColor(0.5f));
                image.setRotationDegrees(-reader.getPageRotation(i));
                document.add(image);
                document.add(new Paragraph("This is page: " + i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }
