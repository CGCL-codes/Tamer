    public void execute() {
        try {
            if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
            File src = (File) getValue("srcfile");
            if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file for the first part of the PDF");
            File dest = (File) getValue("destfile");
            String selection = (String) getValue("selection");
            PdfReader reader = new PdfReader(src.getAbsolutePath());
            System.out.println("The original file had " + reader.getNumberOfPages() + " pages.");
            reader.selectPages(selection);
            int pages = reader.getNumberOfPages();
            System.err.println("The new file has " + pages + " pages.");
            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest.getAbsolutePath()));
            document.open();
            PdfImportedPage page;
            for (int i = 0; i < pages; ) {
                ++i;
                System.out.println("Processed page " + i);
                page = copy.getImportedPage(reader, i);
                copy.addPage(page);
            }
            PRAcroForm form = reader.getAcroForm();
            if (form != null) copy.copyAcroForm(reader);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
