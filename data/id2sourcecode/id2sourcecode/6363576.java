    private static void process(String outFile, List<File> files) throws IOException {
        BufferedWriter os = null;
        try {
            os = new BufferedWriter(new FileWriter(outFile));
            for (File file : files) {
                System.out.println("Processing " + file + " ...");
                int id = Integer.parseInt(getBase(file.toString()));
                SparseFileUtils.writeLine(os, id, readTerms(file));
            }
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
