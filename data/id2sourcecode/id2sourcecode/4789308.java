    public void execute() {
        try {
            if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
            File src = (File) getValue("srcfile");
            File directory = src.getParentFile();
            String name = src.getName();
            name = name.substring(0, name.lastIndexOf('.'));
            PdfReader reader = new PdfReader(src.getAbsolutePath());
            int n = reader.getNumberOfPages();
            int digits = 1 + (n / 10);
            System.out.println("There are " + n + " pages in the original file.");
            Document document;
            int pagenumber;
            String filename;
            for (int i = 0; i < n; i++) {
                pagenumber = i + 1;
                filename = String.valueOf(pagenumber);
                while (filename.length() < digits) filename = "0" + filename;
                filename = "_" + filename + ".pdf";
                document = new Document(reader.getPageSizeWithRotation(pagenumber));
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(directory, name + filename)));
                document.open();
                PdfContentByte cb = writer.getDirectContent();
                PdfImportedPage page = writer.getImportedPage(reader, pagenumber);
                int rotation = reader.getPageRotation(pagenumber);
                if (rotation == 90 || rotation == 270) {
                    cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(pagenumber).getHeight());
                } else {
                    cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                }
                document.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
