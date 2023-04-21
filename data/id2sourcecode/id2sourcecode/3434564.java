    public static void main(String[] args) {
        Document document = new Document(PageSize.POSTCARD);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            Image img = Image.getInstance(RESOURCE);
            img.scaleToFit(PageSize.POSTCARD.getWidth(), 10000);
            img.setAbsolutePosition(0, 0);
            document.add(img);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
