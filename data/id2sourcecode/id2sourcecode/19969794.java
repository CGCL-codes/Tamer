    public static void main(String[] args) throws IOException {
        CsvReader reader = new CsvReader(new FileReader("/tmp/filename_id_list.csv"), ',');
        reader.setTrimWhitespace(true);
        reader.readHeaders();
        while (reader.readRecord()) {
            String filename = reader.get("filename");
            String arxivid = reader.get("arxivid");
            String clearId = extractId(arxivid);
            String newFilename = String.format("%s.tex", clearId).replace("/", "_");
            try {
                FileUtils.copyFile(new File(SAARB_COLLECTION_DIR + filename), new File(PROCESSED_SAARB_DIR + newFilename));
            } catch (Exception e) {
                System.out.println(String.format("failed to rename the file '%s'", filename));
            }
        }
        reader.close();
    }
