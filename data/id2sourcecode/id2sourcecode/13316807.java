    public static void addMeta(File a_fileSrc, File a_fileDest, String a_title, String a_subject, String a_keywords, String a_author, String a_creator) throws DocumentException, IOException {
        Document document = new Document();
        File l_inputFile = a_fileSrc;
        String l_inputPath = l_inputFile.getAbsolutePath();
        File l_outputFile = a_fileDest;
        if (a_fileDest == null) {
            l_outputFile = FTools.replaceIntoPath(a_fileSrc, ".pdf", "-temp.pdf");
        }
        l_outputFile.delete();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(l_outputFile));
        document.open();
        document.addAuthor(a_author);
        document.addCreator(a_creator);
        document.addKeywords(a_keywords);
        document.addTitle(a_title);
        document.addSubject(a_subject);
        PdfReader reader = new PdfReader(l_inputPath);
        int n = reader.getNumberOfPages();
        PdfImportedPage page;
        for (int i = 1; i <= n; i++) {
            page = writer.getImportedPage(reader, i);
            Image instance = Image.getInstance(page);
            document.add(instance);
        }
        document.close();
        reader.close();
        writer.close();
        if (a_fileDest == null) {
            FTools.rename(l_outputFile, a_fileSrc);
        }
    }
