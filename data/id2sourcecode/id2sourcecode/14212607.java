    private void processAlignmentsFromBatchFile(String batchFileLocation) throws IOException, FileNotFoundException, KADMOSCMDException, AlignmentParserException, IllegalArgumentException {
        URL url;
        BufferedReader reader;
        try {
            url = new URL(batchFileLocation);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (Exception e) {
            reader = new BufferedReader(new FileReader(batchFileLocation));
        }
        CSVParser csvParser = new CSVParser(reader, new CSVStrategy(',', '"', '#', (char) -2, true, true, false, true));
        String[][] data = csvParser.getAllValues();
        for (int i = 0; i < data.length; i++) {
            if (data[i].length == 3) {
                processAlignmentsFromAlignmentSource(data[i][0], parseReference(data[i][1]), data[i][2]);
            } else {
                throw new KADMOSCMDException("Bad line in csv file.");
            }
        }
    }
