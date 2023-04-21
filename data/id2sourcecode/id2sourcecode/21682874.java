    public static final void testPDFConcatenate(final PrintStream out, final BufferedReader in, final String oFile, final Collection<String> iFiles) {
        String outFile = oFile;
        while ((null == outFile) || (outFile.length() <= 0)) outFile = getval(out, in, "output file path (or Quit)");
        if (isQuit(outFile)) return;
        Collection<String> inFiles = iFiles;
        while ((null == inFiles) || (inFiles.size() <= 0)) {
            for (int fIndex = 1; ; fIndex++) {
                final String f = getval(out, in, "input file #" + fIndex + " path [ENTER=end/Quit]");
                if ((null == f) || (f.length() <= 0)) break;
                if (isQuit(f)) return;
            }
        }
        int pageOffset = 0;
        Document document = null;
        PdfCopy writer = null;
        List<Object> master = new ArrayList<Object>();
        for (final String f : inFiles) {
            out.println("\tProcessing input file=" + f);
            try {
                final PdfReader reader = new PdfReader(f);
                reader.consolidateNamedDestinations();
                int n = reader.getNumberOfPages();
                List<?> bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) {
                    if (pageOffset != 0) SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    master.addAll(bookmarks);
                }
                pageOffset += n;
                if (null == document) {
                    document = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, new FileOutputStream(outFile));
                    document.open();
                    out.println("\tOpened output=" + outFile);
                }
                for (int i = 1; i <= n; i++) {
                    final PdfImportedPage page = writer.getImportedPage(reader, i);
                    writer.addPage(page);
                }
                final PRAcroForm form = reader.getAcroForm();
                if (form != null) writer.copyAcroForm(reader);
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + " while handle input=" + f + ": " + e.getMessage());
            }
        }
        try {
            if (!master.isEmpty()) writer.setOutlines(master);
            document.close();
            out.println("\tClosing output=" + outFile);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + " while finalize output: " + e.getMessage());
        }
    }
