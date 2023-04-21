    public static void main4(String[] args) throws DocumentException, IOException {
        Document document = new Document();
        File l_dir = new File("B:/gb-data/usana-data/pdf/test");
        File l_outputFile = new File(l_dir, "100b.pdf");
        File l_inputFile = new File(l_dir, "100.pdf");
        String l_inputPath = l_inputFile.getAbsolutePath();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(l_outputFile));
        document.open();
        document.addAuthor("USANA Canada - info@AskforYourHealth.com");
        document.addCreator("USANA Canada - info@AskforYourHealth.com");
        document.addKeywords("aa,bb,cc");
        document.addTitle("HealthPak 100");
        document.addSubject("Sujet HealthPak 100");
        PdfReader reader = new PdfReader(l_inputPath);
        int n = reader.getNumberOfPages();
        PdfImportedPage page;
        for (int i = 1; i <= n; i++) {
            page = writer.getImportedPage(reader, i);
            Image instance = Image.getInstance(page);
            document.add(instance);
        }
        document.close();
    }
