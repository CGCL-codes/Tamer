    private String readFile(String name) throws IOException {
        final StringWriter stringOut = new StringWriter();
        executeZipEntryAction(name, new ZipEntryAction() {

            public void execute(ZipFile file, ZipEntry entry) throws IOException {
                InputStream entryIn = null;
                try {
                    entryIn = new BufferedInputStream(file.getInputStream(entry));
                    int c;
                    while ((c = entryIn.read()) != -1) stringOut.write(c);
                } finally {
                    stringOut.close();
                    if (entryIn != null) entryIn.close();
                }
            }
        });
        return stringOut.toString();
    }
