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
            pagecountinsertedpages = 0;
            pagecountrotatedpages = 0;
            pagecount = 0;
            PdfReader reader = new PdfReader(src.getAbsolutePath());
            PdfStamper stp = new PdfStamper(reader, new FileOutputStream(dest));
            PdfWriter writer = stp.getWriter();
            ArrayList<PdfDictionary> pageInh = new ArrayList<PdfDictionary>();
            PdfDictionary catalog = reader.getCatalog();
            PdfDictionary rootPages = catalog.getAsDict(PdfName.PAGES);
            iteratePages(rootPages, reader, pageInh, 0, writer);
            if (((pagecount) % 2) == 1) {
                appendemptypageatend(reader, writer);
                this.pagecountinsertedpages++;
            }
            stp.close();
            System.out.println("In " + dest.getAbsolutePath() + " pages= " + pagecount + " inserted pages=" + this.getPagecountinsertedpages() + " rotated pages=" + this.getPagecountrotatedpages());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
