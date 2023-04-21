    public void execute() {
        try {
            if (getValue("srcfile") == null) {
                throw new InstantiationException("You need to choose a sourcefile");
            }
            File src = (File) getValue("srcfile");
            if (getValue("destfile") == null) {
                throw new InstantiationException("You need to choose a destination file");
            }
            File dest = (File) getValue("destfile");
            PdfReader reader = new PdfReader(src.getAbsolutePath());
            int total = reader.getNumberOfPages();
            System.out.println("There are " + total + " pages in the original file.");
            Rectangle pageSize = reader.getPageSize(1);
            Rectangle newSize = new Rectangle(pageSize.getWidth() / 2, pageSize.getHeight());
            Document document = new Document(newSize, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            float offsetX, offsetY;
            int p;
            for (int i = 0; i < total; i++) {
                p = i + 1;
                pageSize = reader.getPageSize(p);
                newSize = new Rectangle(pageSize.getWidth() / 2, pageSize.getHeight());
                document.newPage();
                offsetX = 0;
                offsetY = 0;
                page = writer.getImportedPage(reader, p);
                cb.addTemplate(page, 1, 0, 0, 1, offsetX, offsetY);
                document.newPage();
                offsetX = -newSize.getWidth();
                offsetY = 0;
                page = writer.getImportedPage(reader, p);
                cb.addTemplate(page, 1, 0, 0, 1, offsetX, offsetY);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
