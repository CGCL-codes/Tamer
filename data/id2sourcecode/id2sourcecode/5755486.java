    public void testConvert() {
        try {
            File csvFile = new File("C:/DE311/solution_workspace/WorkbookTaglib/WorkbookTagDemoWebapp/src/main/resources/csv/test.csv");
            Charset guessedCharset = com.glaforge.i18n.io.CharsetToolkit.guessEncoding(csvFile, 4096);
            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), guessedCharset)));
            PDFWriter writer = new PDFWriter(new File("/temp/test.pdf"));
            int nbTotalRows = CsvConverterUtils.countLines(new BufferedReader(new FileReader(csvFile)));
            int[] colsCharCount = new int[] { 10, 10, 20, 7, 2, 5 };
            PDFConverter pdfConv = new PDFConverter();
            pdfConv.convert(reader, writer, nbTotalRows, colsCharCount, "1|1|1|1|1|1|", Paper.LEGAL_LANDSCAPE);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
