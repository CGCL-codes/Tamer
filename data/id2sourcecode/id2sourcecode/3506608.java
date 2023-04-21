    public void execute() {
        try {
            if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
            File src = (File) getValue("srcfile");
            if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
            File dest = (File) getValue("destfile");
            int pow2;
            try {
                pow2 = Integer.parseInt((String) getValue("pow2"));
            } catch (Exception e) {
                pow2 = 1;
            }
            PdfReader reader = new PdfReader(src.getAbsolutePath());
            int total = reader.getNumberOfPages();
            System.out.println("There are " + total + " pages in the original file.");
            Rectangle pageSize = reader.getPageSize(1);
            Rectangle newSize = (pow2 % 2) == 0 ? new Rectangle(pageSize.getWidth(), pageSize.getHeight()) : new Rectangle(pageSize.getHeight(), pageSize.getWidth());
            Rectangle unitSize = new Rectangle(pageSize.getWidth(), pageSize.getHeight());
            Rectangle currentSize;
            for (int i = 0; i < pow2; i++) {
                unitSize = new Rectangle(unitSize.getHeight() / 2, unitSize.getWidth());
            }
            int n = (int) Math.pow(2, pow2);
            int r = (int) Math.pow(2, pow2 / 2);
            int c = n / r;
            Document document = new Document(newSize, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            float offsetX, offsetY, factor;
            int p;
            for (int i = 0; i < total; i++) {
                if (i % n == 0) {
                    document.newPage();
                }
                p = i + 1;
                offsetX = unitSize.getWidth() * ((i % n) % c);
                offsetY = newSize.getHeight() - (unitSize.getHeight() * (((i % n) / c) + 1));
                currentSize = reader.getPageSize(p);
                factor = Math.min(unitSize.getWidth() / currentSize.getWidth(), unitSize.getHeight() / currentSize.getHeight());
                offsetX += (unitSize.getWidth() - (currentSize.getWidth() * factor)) / 2f;
                offsetY += (unitSize.getHeight() - (currentSize.getHeight() * factor)) / 2f;
                page = writer.getImportedPage(reader, p);
                cb.addTemplate(page, factor, 0, 0, factor, offsetX, offsetY);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
