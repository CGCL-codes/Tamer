    public static void main(String[] args) {
        Document document = new Document();
        System.out.println("A4.getHeight() = " + PageSize.A4.getHeight());
        System.out.println("leftMargin = " + document.leftMargin());
        System.out.println("rightMargin = " + document.rightMargin());
        System.out.println("topMargin = " + document.topMargin());
        System.out.println("bottomMargin = " + document.bottomMargin());
        try {
            PdfReader reader = new PdfReader("file/emmanuel-text.pdf");
            int numPages = reader.getNumberOfPages();
            System.out.println("numpages = " + numPages);
            int N = (int) Math.ceil(numPages / 8f);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("file/emmanuel-text-a6.pdf"));
            document.setPageSize(PageSize.A4);
            document.setMargins(36f, 36f, 36f, 36f);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            int pageNum = 0;
            for (int i = 0; i < N; i++) {
                pageNum = 2 * i + 1;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, PageSize.A4.getWidth() / 2f, 0f);
                }
                pageNum = (8 * N + 1) - pageNum;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, 0f, 0f);
                }
                pageNum = 2 * N + 1 + 2 * i;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, PageSize.A4.getWidth() / 2f, PageSize.A4.getHeight() / 2f);
                }
                pageNum = (8 * N + 1) - pageNum;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, 0, PageSize.A4.getHeight() / 2f);
                }
                document.newPage();
                pageNum = 2 * i + 2;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, 0f, 0f);
                }
                pageNum = (8 * N + 1) - pageNum;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, PageSize.A4.getWidth() / 2f, 0f);
                }
                pageNum = 2 * N + 2 + 2 * i;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, 0, PageSize.A4.getHeight() / 2f);
                }
                pageNum = (8 * N + 1) - pageNum;
                if (pageNum <= numPages) {
                    page = writer.getImportedPage(reader, pageNum);
                    cb.addTemplate(page, (float) 0.5f, 0f, 0f, (float) 0.5f, PageSize.A4.getWidth() / 2f, PageSize.A4.getHeight() / 2f);
                }
                document.newPage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }
