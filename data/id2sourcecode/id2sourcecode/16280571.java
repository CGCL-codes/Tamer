    public void execute() {
        try {
            File[] files;
            if (getValue("srcfiles") == null) {
                throw new InstantiationException("You need to choose a list of sourcefiles");
            }
            files = ((File[]) getValue("srcfiles"));
            if (getValue("destfile") == null) {
                throw new InstantiationException("You need to choose a destination file");
            }
            File pdf_file = (File) getValue("destfile");
            int pageOffset = 0;
            ArrayList<HashMap<String, Object>> master = new ArrayList<HashMap<String, Object>>();
            Document document = null;
            PdfCopy writer = null;
            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(files[i].getAbsolutePath());
                reader.consolidateNamedDestinations();
                int n = reader.getNumberOfPages();
                List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) {
                    if (pageOffset != 0) {
                        SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    }
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
            if (!master.isEmpty()) {
                writer.setOutlines(master);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
