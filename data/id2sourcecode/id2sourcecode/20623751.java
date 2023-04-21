    public static void main(String[] args) {
        try {
            PdfReader reader = new PdfReader(NEWSPAPER);
            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            PdfContentByte content = writer.getDirectContent();
            content.rectangle(document.left(), document.bottom(), document.right(), document.top());
            content.rectangle(LLX1, LLY1, W1, H1);
            content.rectangle(LLX2, LLY2, W2, H2);
            content.eoClip();
            content.newPath();
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            content.addTemplate(page, 0, 0);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
