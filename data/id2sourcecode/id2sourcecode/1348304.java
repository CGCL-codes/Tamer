    public void execute() {
        try {
            String[] files = new String[2];
            if (getValue("srcfile1") == null) throw new InstantiationException("You need to choose a first sourcefile");
            files[0] = ((File) getValue("srcfile1")).getAbsolutePath();
            if (getValue("srcfile2") == null) throw new InstantiationException("You need to choose a second sourcefile");
            files[1] = ((File) getValue("srcfile2")).getAbsolutePath();
            if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
            File pdf_file = (File) getValue("destfile");
            int pageOffset = 0;
            List<HashMap<String, Object>> master = new ArrayList<HashMap<String, Object>>();
            Document document = null;
            PdfCopy writer = null;
            for (int i = 0; i < 2; i++) {
                PdfReader reader = new PdfReader(files[i]);
                reader.consolidateNamedDestinations();
                int n = reader.getNumberOfPages();
                List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) {
                    if (pageOffset != 0) SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    master.addAll(bookmarks);
                }
                pageOffset += n;
                System.out.println("There are " + n + " pages in " + files[i]);
                if (i == 0) {
                    document = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, new FileOutputStream(pdf_file));
                    document.open();
                }
                PdfImportedPage page;
                for (int p = 0; p < n; ) {
                    ++p;
                    page = writer.getImportedPage(reader, p);
                    writer.addPage(page);
                    System.out.println("Processed page " + p);
                }
            }
            if (!master.isEmpty()) writer.setOutlines(master);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
