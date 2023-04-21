    public void convert(CSVReader reader, PDFWriter writer, int nbTotalRows, int[] colsCharCount, String selectedCols, Paper paper) throws IOException, InterruptedException {
        Validate.notNull(reader, "CSVReader");
        Validate.notNull(writer, "Writer");
        String[] selCols = StringUtils.split(selectedCols, '|');
        int nbColumns = StringUtils.countMatches(selectedCols, "1");
        int nbColumnsTotal = Math.min(20, colsCharCount.length);
        double[] pdfColsWidths = new double[nbColumns];
        double totalWidths = 0;
        int c = 0;
        for (int i = 0; i < nbColumnsTotal; i++) {
            if (i < selCols.length && "1".equals(selCols[i])) {
                pdfColsWidths[c] = Math.min(COL_WIDTH_MAX, Math.round((double) colsCharCount[i] / (double) NB_CHAR_PER_INCH));
                totalWidths += pdfColsWidths[c];
                c++;
            }
        }
        Font normalFont = new Font(Font.HELVETICA, 8);
        Column[] cols = new Column[nbColumns];
        double printableWidth = paper.getWidth() - (paper.getHorizontalMargin() * 2);
        for (int i = 0; i < nbColumns; i++) {
            double proportionalWidth = (pdfColsWidths[i] / totalWidths) * printableWidth;
            double width = Math.max(0.9, proportionalWidth);
            cols[i] = new Column(width, Alignment.LEFT, normalFont);
        }
        Font tableHeaderFont = new Font(Font.HELVETICA_BOLD_ITALIC, 8);
        Heading[] hdrs = new Heading[nbColumns];
        int currentRow = 1;
        String[] nextLine = reader.readNext();
        c = 0;
        for (int i = 0; i < nbColumnsTotal; i++) {
            if ("1".equals(selCols[i])) {
                hdrs[c] = new Heading(nextLine[i], Alignment.LEFT, tableHeaderFont);
                c++;
            }
        }
        Document doc = new Document(paper, writer);
        doc.open();
        Table table = doc.createTable(cols, hdrs);
        table.setHeaderBackground(Colour.GREY_80);
        table.setBorder(1);
        table.setHeaderBorder(1, 1);
        table.setColumnBorder(1);
        table.setRowBorder(1);
        while (currentRow < nbTotalRows) {
            currentRow++;
            if (progress != null) {
                float percent = ((float) currentRow / (float) nbTotalRows) * 100f;
                progress.updateProgress(ConvertionStepEnum.PROCESSING_ROWS, percent);
            }
            nextLine = reader.readNext();
            if (nextLine == null) {
                break;
            }
            String[] tmp = new String[nbColumns];
            for (int i = 0; i < nbColumns; i++) {
                tmp[i] = "";
            }
            c = 0;
            for (int i = 0; i < nbColumnsTotal; i++) {
                if ("1".equals(selCols[i])) {
                    tmp[c] = nextLine[i];
                    c++;
                }
            }
            table.addRow(tmp);
            MemoryUtils.doGC();
        }
        table.close();
        doc.close();
        reader.close();
        if (progress != null) {
            progress.updateProgress(ConvertionStepEnum.PROCESSING_NAVIGATION, 100);
            Thread.sleep(50);
        }
    }
