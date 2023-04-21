    public int[] convert(CSVReader reader, File firstFile, int maxPerPage, int nbTotalRows, String pageBegin, String pageEnd) throws IOException, InterruptedException {
        Validate.notNull(reader, "CSVReader");
        Validate.notNull(firstFile, "firstFile");
        Validate.isTrue(maxPerPage > 0, "maxPerPage == 0");
        Validate.isTrue(nbTotalRows > 0, "nbTotalRows == 0");
        final File dir = firstFile.getParentFile();
        String nextFilename = StringUtils.replace(firstFile.getName(), ".html", ".tmp");
        File currentFile = new File(dir, nextFilename);
        Writer w = new BufferedWriter(new FileWriter(currentFile));
        int currentFileNumber = 1;
        List<File> files = new ArrayList<File>();
        files.add(currentFile);
        int currentRow = 1;
        String[] nextLine = reader.readNext();
        int[] colsCharCount = new int[nextLine.length];
        String header = buildHeaderLine(nextLine);
        w.write(header);
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
            writeTableRow(w, nextLine, colsCharCount);
            if (((currentRow - 1) % maxPerPage) == 0) {
                currentFileNumber++;
                w.flush();
                w.close();
                nextFilename = StringUtils.replace(firstFile.getName(), ".html", currentFileNumber + ".tmp");
                currentFile = new File(dir, nextFilename);
                files.add(currentFile);
                w = new BufferedWriter(new FileWriter(currentFile));
                w.write(header);
            }
        }
        if (w != null) {
            w.flush();
            w.close();
        }
        MemoryUtils.doGC();
        if (progress != null) {
            progress.updateProgress(ConvertionStepEnum.PROCESSING_NAVIGATION, 100);
            Thread.sleep(50);
        }
        completePagesNavigation(files, maxPerPage, nbTotalRows, pageBegin, pageEnd, colsCharCount);
        return colsCharCount;
    }
