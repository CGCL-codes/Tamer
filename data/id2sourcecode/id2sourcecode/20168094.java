    public static void main(String[] args) {
        System.getProperty("os.name");
        try {
            PdfReader reader = new PdfReader("/home/asl/books/hibernate_In_Action.pdf");
            PdfTextExtractor pdfTextExtractor = new PdfTextExtractor(reader);
            String text = pdfTextExtractor.getTextFromPage(1);
            System.out.println(text);
            Map info = reader.getInfo();
            for (Iterator i = info.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                String value = (String) info.get(key);
                System.out.println(key + ": " + value);
            }
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("HelloWorldImportedPages.pdf"));
            document.open();
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            Image image = Image.getInstance(page);
            byte b[] = image.getOriginalData();
            int type = image.getOriginalType();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
