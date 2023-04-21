    private boolean readline(BufferedReader reader) throws Exception {
        BufferedWriter writer = null;
        int c;
        try {
            c = reader.read();
            if (c == -1) return false;
            lineNum++;
            while (c == '\r') c = reader.read();
            if (c == '\n') {
                return true;
            }
            int i = 0;
            StringBuffer name = new StringBuffer();
            while (true) {
                name.append((char) c);
                if (i > 255 - EXT.length()) {
                    break;
                }
                if ((c = reader.read()) == -1) return false;
                if (c == '\n') return true;
                while (c == '\r') c = reader.read();
                if (c == FIELDS_DELIMITER) {
                    break;
                }
            }
            for (i = 0; i < 5; i++) {
                while (true) {
                    c = reader.read();
                    while (c == '\r') c = reader.read();
                    if (c == -1) return false;
                    if (c == '\n') return true;
                    if (c == FIELDS_DELIMITER) {
                        break;
                    }
                }
            }
            String outputFilePath;
            String outputDir = Parameters.getString(Constants.OUTPUT_DIR_KEY);
            if (outputDir != null) {
                File file = new File(outputDir);
                if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
                    System.out.println("Directory " + outputDir + " does not exist or you do not have permissions to write into it.");
                    System.out.println();
                    return false;
                }
                outputFilePath = outputDir + File.separator + name + EXT;
            } else {
                outputFilePath = name + EXT;
            }
            if (Main.trace) System.out.println(outputFilePath);
            File outFile = new File(outputFilePath);
            writer = new BufferedWriter(new FileWriter(outFile, false));
            writer.append(getHeader());
            return read_genome(reader, writer);
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
