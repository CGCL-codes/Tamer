    public void processFile(File inFile, String inEncoding, File outFile, String outEncoding) throws IOException, TranslationException {
        BufferedReader reader = createReader(inFile, inEncoding);
        try {
            BufferedWriter writer;
            if (outFile != null) {
                writer = createWriter(outFile, outEncoding);
            } else {
                writer = null;
            }
            try {
                processFile(reader, writer);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } finally {
            reader.close();
        }
    }
