    private void parse() throws IOException {
        HTMLLinkIndexer indexer = getHTMLEntities();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        InputStream data = null;
        StringBuffer input = new StringBuffer(512);
        try {
            data = this.compilationUnit.getSource().getInputStream();
            String encoding = GlobalOptions.getEncoding();
            reader = new BufferedReader(new InputStreamReader(data, encoding));
            writer = new BufferedWriter(new FileWriter(createFileName(this.compilationUnit.getSource().getRelativePath())));
            printHeader(writer, this.compilationUnit.getSource().getRelativePath());
            printContent(indexer, reader, writer, input);
            printFooter(writer);
        } catch (Exception e) {
            System.err.println("reader: " + reader);
            System.err.println("writer: " + writer);
            System.err.println("out: " + createFileName(this.compilationUnit.getSource().getRelativePath()));
            e.printStackTrace();
            throw new RuntimeException("The HTML printer crashed unexpectedly, check the console for a stack trace.");
        } finally {
            if (data != null) {
                data.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
