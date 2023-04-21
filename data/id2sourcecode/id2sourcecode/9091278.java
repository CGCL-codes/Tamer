    public static void main(String[] args) {
        try {
            createOneCard();
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            PdfReader reader = new PdfReader(CARD);
            document.open();
            PdfContentByte canvas = writer.getDirectContent();
            canvas.addTemplate(writer.getImportedPage(reader, 1), 36, 600);
            canvas.addTemplate(writer.getImportedPage(reader, 2), 200, 600);
            canvas.moveTo(0, 600);
            canvas.lineTo(595, 600);
            canvas.stroke();
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
