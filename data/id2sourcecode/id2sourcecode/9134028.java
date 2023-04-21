    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("arguments: file1 [file2 ...] destfile");
        } else {
            System.out.println("PdfCopy example");
            try {
                int pageOffset = 0;
                ArrayList master = new ArrayList();
                int f = 0;
                String outFile = args[args.length - 1];
                Document document = null;
                PdfCopy writer = null;
                while (f < args.length - 1) {
                    PdfReader reader = new PdfReader(args[f]);
                    reader.consolidateNamedDestinations();
                    int n = reader.getNumberOfPages();
                    List bookmarks = SimpleBookmark.getBookmark(reader);
                    if (bookmarks != null) {
                        if (pageOffset != 0) SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                        master.addAll(bookmarks);
                    }
                    pageOffset += n;
                    if (f == 0) {
                        document = new Document(reader.getPageSizeWithRotation(1));
                        writer = new PdfCopy(document, new FileOutputStream(outFile));
                        document.open();
                    }
                    PdfImportedPage page;
                    for (int i = 0; i < n; ) {
                        ++i;
                        page = writer.getImportedPage(reader, i);
                        writer.addPage(page);
                    }
                    PRAcroForm form = reader.getAcroForm();
                    if (form != null) writer.copyAcroForm(reader);
                    f++;
                }
                if (!master.isEmpty()) writer.setOutlines(master);
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
